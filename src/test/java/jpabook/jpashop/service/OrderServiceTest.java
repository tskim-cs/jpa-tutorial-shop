package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void order_item() throws Exception {
        // Given
        Member member = createMember();

        int bookPrice = 10000;
        int bookStock = 10;
        Item item = createBook("JPA", bookPrice, bookStock);

        int orderCount = 2;

        // When
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // Then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("OrderStatus should be ORDER.", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("The number of ordered item should be same.", 1, getOrder.getOrderItems().size());
        assertEquals("Ordered price should be orderPrice * count", orderCount * bookPrice, getOrder.getTotalPrice());
        assertEquals("Stock qunatity should be decreased after order", bookStock - orderCount, item.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void order_item_exceed_stock() throws Exception {
        // Given
        Member member = createMember();

        int bookPrice = 10000;
        int bookStock = 10;
        Item item = createBook("JPA", bookPrice, bookStock);

        int orderCount = 11;

        // When
        orderService.order(member.getId(), item.getId(), orderCount);

        // Then
        fail("Exception should be occured.");
    }
    
    @Test
    public void cancel_order() throws Exception {
        // Given
        Member member = createMember();

        int bookPrice = 10000;
        int bookStock = 10;
        Item item = createBook("JPA", bookPrice, bookStock);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // When
        orderService.cancelOrder(orderId);

        // Then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("Order status should be CANCEL.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("Stock quantity should be increased after cancel", bookStock, item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stock) {
        Book book = new Book();

        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stock);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("mem1");
        member.setAddress(new Address("Seoul", "Ro", "12345"));
        em.persist(member);
        return member;
    }
        
}