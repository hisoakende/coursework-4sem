import React, { useState, useEffect } from 'react';

const MyOrders = () => {
  const [orders, setOrders] = useState([]);

  useEffect(() => {
    // Функция для загрузки данных о заказах пользователя
    const fetchOrders = async () => {
      try {
        const response = await fetch('http://127.0.0.1:8080/orders');
        if (!response.ok) {
          // Обработка ошибочного ответа
          throw new Error('Network response was not ok');
        }
        const data = await response.json();
        setOrders(data);
      } catch (error) {
        // Обработка ошибки загрузки данных
        console.error('There was a problem with the fetch operation:', error);
      }
    };

    fetchOrders();
  }, []);

  return (
    <div className='ticket'>
      <h1>Мои заказы</h1>
      <ul>
        {orders.map((order) => (
          <li key={order.id}>
            <div className='ticket-field'>Отправление: {order.ticket.departureAirport}</div>
            <div className='ticket-field'>Прибытие: {order.ticket.arrivalAirport}</div>
            <div className='ticket-field'>Дата и время вылета: {order.ticket.departureDatetime}</div>
            <div className='ticket-field'>Дата и время прилета: {order.ticket.arrivalDatetime}</div>
            <div className='ticket-field'>Авиакомпания: {order.ticket.airline}</div>
            <div className='ticket-field'>Стоимость билета: {order.ticket.price} руб.</div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default MyOrders;
