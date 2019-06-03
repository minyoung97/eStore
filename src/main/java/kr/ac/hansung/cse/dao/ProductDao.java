package kr.ac.hansung.cse.dao;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.hansung.cse.model.Product;
@Repository
@Transactional
public class ProductDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Product getProductById(int id) {

		Session session = sessionFactory.getCurrentSession();
		// 하나의 레코드를 읽을때 클래스 이름과 id를 얻어온다.
		Product product = (Product) session.get(Product.class, id);

		return product;
	}

	@SuppressWarnings("unchecked")
	public List<Product> getProducts() {

		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Product");
		List<Product> productList = query.list();

		return productList;

	}

	public void addProduct(Product product) {// 여기서 실제로 디비에 저장된다.

		Session session = sessionFactory.getCurrentSession();
		// saveOrUpdate는 리턴타입은 void이다.
		session.saveOrUpdate(product);
		// 해도 그만, 트랜젝션에의해 자동적으로 커밋을해서 플러시가 이뤄지기때문에.
		session.flush();

	}

	public void deleteProduct(Product product) {

		Session session = sessionFactory.getCurrentSession();
		session.delete(product);
		session.flush();

	}

	public void updateProduct(Product product) {

		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(product);
		session.flush();

	}
}
