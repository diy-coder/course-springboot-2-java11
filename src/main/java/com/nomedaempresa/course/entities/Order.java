package com.nomedaempresa.course.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nomedaempresa.course.entities.enums.OrderStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_order")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Order extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "GMT")
	private Instant moment;

	private Integer orderStatus;
	
	@ManyToOne
	@JoinColumn(name = "client_id")
	private User client;

	@OneToMany(mappedBy = "id.order")
	private Set<OrderItem> items = new HashSet<>();
	
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private Payment payment;

	public Order(Long id, Instant moment, OrderStatus orderStatus, User client) {
		this.id = id;
		this.moment = moment;
		setOrderStatus(orderStatus);
		this.setClient(client);
	}

	public OrderStatus getOrderStatus() {
		return OrderStatus.valueOf(orderStatus);
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		if (orderStatus != null) {
			this.orderStatus = orderStatus.getCode();	
		}
	}
	
	public Double getTotal() {
		double sum = 0.0;
		for (OrderItem x : items) {
			sum += x.getSubTotal();
		}
		return sum;
	}

}
