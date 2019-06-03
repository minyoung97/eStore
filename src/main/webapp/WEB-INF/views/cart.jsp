<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script src="<c:url value="/resources/js/controller.js" /> "></script>

<div class="container-wrapper">
	<div class="container">
		<div class="jumbotron">
			<div class="container">
				<h2>Cart</h2>

				<p>All the selected products in your shopping cart</p>
			</div>
		</div>


		<section class="container" ng-app="cartApp">
			<div ng-controller="cartCtrl" ng-init="initCartId('${cartId}')">
				<button type="button" class="btn btn-danger" ng-click="clearCart()">
					<i class="fa fa-times-circle-o"></i> Clear Cart
				</button>
				<br />

				<table class="table table-hover">

					<tr>
						<th>Product</th>
						<th>Unit Price</th>
						<th>Quantity</th>
						<th>Total Price</th>
						<th>Action</th>
					</tr>


					<tr ng-repeat="item in cart.cartItems">
						<td>{{item.product.name}}</td>
						<td>{{item.product.price}}</td>
						<td>{{item.quantity}}</td>
						<td>{{item.totalPrice}}</td>
						<td>

							<button type="button" class="btn btn-danger btn-sm"
								ng-click="removeFromCart(item.product.id)">
								<i class="fa fa-times"></i>remove
							</button>
							<button type="button" class="btn btn-danger btn-sm"
								ng-click="plusQuantity(item.product.id)">
								<i class="fa fa-plus"></i>plus
							</button>
							<button type="button" class="btn btn-danger btn-sm"
								ng-click="minusQuantity(item.product.id)">
								<i class="fa fa-minus"></i>minus
							</button>

						</td>

					</tr>

					<tr>
						<td></td>
						<td></td>
						<td>Grand Total</td>
						<td>{{calGrandTotal()}}</td>
						<td></td>
					</tr>

				</table>
				
				<a class="btn btn-warning btn-large" href="<c:url value="/products"/>">Continue
					Shopping</a>
			</div>
		</section>

	</div>
</div>