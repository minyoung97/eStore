package kr.ac.hansung.cse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Cart;
import kr.ac.hansung.cse.model.CartItem;
import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.model.User;
import kr.ac.hansung.cse.service.CartItemService;
import kr.ac.hansung.cse.service.CartService;
import kr.ac.hansung.cse.service.ProductService;
import kr.ac.hansung.cse.service.UserService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api/cart")
public class CartRestController {

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private UserService userService;

	@Autowired
	private ProductService productService;

	// 조회
	@RequestMapping(value = "/{cartId}", method = RequestMethod.GET)
	public ResponseEntity<Cart> getCartById(@PathVariable(value = "cartId") int cartId) {
		Cart cart = cartService.getCartById(cartId);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}

	// 카트에 있는것 모두 삭제 - 카트를 비운다
	@RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> clearCart(@PathVariable(value = "cartId") int cartId) {
		Cart cart = cartService.getCartById(cartId);
		cartItemService.removeAllCartItems(cart);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// 카트에 productId 담는다
	@RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
	public ResponseEntity<Void> addItem(@PathVariable(value = "productId") int productId) {

		Product product = productService.getProductById(productId);

		// 현재 인증된 사용자 정보를 가져옴
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = userService.getUserByUsername(username);
		Cart cart = user.getCart();

		// check if cartitem for a given product already exists
		// 카트에 이미 상품이 있는 경우
		// 이미 가지고 있으면 숫자만 증가시키자
		List<CartItem> cartItems = cart.getCartItems();

		for (int i = 0; i < cartItems.size(); i++) {
			if (product.getId() == cartItems.get(i).getProduct().getId()) {
				CartItem cartItem = cartItems.get(i);
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
				cartItemService.addCartItem(cartItem);

				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		// create new cartItem
		// 새로운 상품일 때
		CartItem cartItem = new CartItem();
		cartItem.setQuantity(1);
		cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
		cartItem.setProduct(product);
		cartItem.setCart(cart);

		// bidirectional
		cart.getCartItems().add(cartItem);

		cartItemService.addCartItem(cartItem);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	// 하나의 항목 상품 삭제
	@RequestMapping(value = "/cartitem/{productId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> removeItem(@PathVariable(value = "productId") int productId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = userService.getUserByUsername(username);
		Cart cart = user.getCart();

		CartItem cartItem = cartItemService.getCartItemByProductId(cart.getId(), productId);
		cartItemService.removeCartItem(cartItem);

		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

	// 수량 증가
		@RequestMapping(value = "/cartitem/plus/{productId}", method = RequestMethod.GET)
		public ResponseEntity<Void> plusQuantity(@PathVariable(value = "productId") int productId) {

			Product product = productService.getProductById(productId);
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();

			User user = userService.getUserByUsername(username);
			Cart cart = user.getCart();

			CartItem cartItem = cartItemService.getCartItemByProductId(cart.getId(), productId);
			
			if(cartItem.getQuantity() < product.getUnitInStock()) {
				cartItem.setQuantity(cartItem.getQuantity() + 1);
				cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
				cartItemService.addCartItem(cartItem);
			}
			
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

		}


	// 수량 감소
	@RequestMapping(value = "/cartitem/minus/{productId}", method = RequestMethod.GET)
	public ResponseEntity<Void> minusQuantity(@PathVariable(value = "productId") int productId) {

		Product product = productService.getProductById(productId);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		User user = userService.getUserByUsername(username);
		Cart cart = user.getCart();

		CartItem cartItem = cartItemService.getCartItemByProductId(cart.getId(), productId);
		
		if(0 < cartItem.getQuantity()) {
			cartItem.setQuantity(cartItem.getQuantity() - 1);
			cartItem.setTotalPrice(product.getPrice() * cartItem.getQuantity());
			cartItemService.addCartItem(cartItem);
		}
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

	}

}
