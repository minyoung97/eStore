package kr.ac.hansung.cse.dao;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.ac.hansung.cse.model.Cart;
import kr.ac.hansung.cse.model.CartItem;

@Repository
@Transactional
public class CartItemDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void addCartItem(CartItem cartItem) {
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(cartItem);
		session.flush();

	}

	public void removeCartItem(CartItem cartItem) {
		Session session = sessionFactory.getCurrentSession();
		session.delete(cartItem);
		session.flush();

	}

	public void removeAllCartItems(Cart cart) {
		List<CartItem> cartItems = cart.getCartItems();// fetch=FetchType.EAGER 줘서 가능

		for (CartItem item : cartItems) {
			removeCartItem(item);
		}

	}

	public CartItem getCartItemByProductId(int cartId, int productId) {
		Session session = sessionFactory.getCurrentSession();
		TypedQuery<CartItem> query = session.createQuery("from CartItem where cart.id=? and product.id=?");
		query.setParameter(0, cartId);
		query.setParameter(1, productId);
		return (CartItem) query.getSingleResult();
	}

}
