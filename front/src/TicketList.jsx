import React, { useState, useEffect } from 'react';
import axios from 'axios';
// Импортируйте дополнительные компоненты, если они у вас уже реализованы
// Далее идёт компонент TicketsList, как в предыдущем примере...



const TicketList = () => {
    const [tickets, setTickets] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [orderedTickets, setOrderedTickets] = useState({});
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize] = useState(25); // Установите желаемый размер страницы
    const [totalPages, setTotalPages] = useState(0);
    const [filters, setFilters] = useState({
        minPrice: '',
        maxPrice: '',
        departureTime: '',
        arrivalTime: '',
        departureAirport: '',
        arrivalAirport: '',
        airline: ''
    });
    const [isFiltersVisible, setIsFiltersVisible] = useState(false);

    const [sortField, setSortField] = useState('price'); // Поле сортировки
    const [sortOrder, setSortOrder] = useState('asc'); // Порядок сортировки

    // При нажатии на кнопку показать/скрыть фильтры
    const toggleFiltersVisibility = () => {
        setIsFiltersVisible(!isFiltersVisible);
    };

    const [airports, setAirports] = useState({
        departure: [],
        arrival: []
    });
    const [airlines, setAirlines] = useState([]);

    useEffect(() => {
        const fetchFilterValues = async () => {
            try {
                const response = await axios.get('http://127.0.0.1:8080/tickets/filters');
                setAirports({
                    departure: response.data.departureAirport,
                    arrival: response.data.arrivalAirport
                });
                setAirlines(response.data.airline);
            } catch (error) {
                console.error('Ошибка при загрузке значений фильтров:', error);
            }
        };

        fetchFilterValues();
    }, []);

    useEffect(() => {
        const storedOrders = {};
        tickets.forEach(ticket => {
            const orderData = localStorage.getItem(`ticket_${ticket.id}`);
            storedOrders[ticket.id] = orderData ? JSON.parse(orderData).ordered : false;
        });
        setOrderedTickets(storedOrders);
    }, [tickets]); // Зависимость `[tickets]` гарантирует, что эффект выполнится при изменении билетов.



    const checkIfTicketOrdered = (ticketId) => {
        const ticketOrderInfo = localStorage.getItem(`ticket_${ticketId}`);
        if (ticketOrderInfo) {
            return JSON.parse(ticketOrderInfo).ordered;
        }
        return false;
    };

    const fetchTickets = async () => {
        setIsLoading(true);
        setError(null);

        const cleanFilters = (filters) => {
            const cleanedFilters = {};
            Object.keys(filters).forEach(key => {
                if (filters[key] || filters[key] === 0) { // Учитываем, что 0 может быть валидным значением
                    cleanedFilters[key] = filters[key];
                }
            });
            return cleanedFilters;
        };

        const cleanedFilters = cleanFilters(filters);
        const getSortParam = () => (sortOrder === 'desc' ? '-' : '') + sortField;
        try {
            const response = await axios.get(`http://127.0.0.1:8080/tickets?page=${currentPage}&size=${pageSize}&sort=${getSortParam()}`, {

                params: cleanedFilters
            });
            setTickets(response.data.content); // предполагая, что сервер возвращает страницу и данные находятся в 'content'
            setIsLoading(false);
            setTotalPages(response.data.totalPages)
        } catch (error) {
            setError(error.message);
            setIsLoading(false);
        }
    };

    useEffect(() => {
        fetchTickets();
    }, [currentPage, filters, sortField, sortOrder]);


    const handleNextPage = () => {
        setCurrentPage(prevPage => prevPage + 1);
    };

    const handlePreviousPage = () => {
        setCurrentPage(prevPage => prevPage - 1);
    };

    const handleOrderTicket = async (ticketId) => {
        try {
            // Предположим, что вы делаете запрос на сервер
            const response = await fetch('http://127.0.0.1:8080/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // Добавьте необходимые заголовки, например авторизации
                },
                body: JSON.stringify({ ticketId: ticketId, username: "aboba1" })
            });

            if (!response.ok) {
                throw new Error('Произошла ошибка при оформлении заказа.');
            }

            alert('Спасибо за ваш заказ! С вами скоро свяжется оператор.');

            // Запишем информацию о заказе в localStorage
            localStorage.setItem(`ticket_${ticketId}`, JSON.stringify({ ordered: true }));

            setOrderedTickets(prevState => ({
                ...prevState,
                [ticketId]: true
            }));

        } catch (error) {
            console.error('Ошибка заказа:', error);
        }
    };

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setFilters(prevFilters => ({ ...prevFilters, [name]: value }));
    };

    return (
        <div className='ticketContainer'>
            <div className={`filter-form${isFiltersVisible ? ' open' : ''}`}>
                {/* Форма с фильтрами */}
                <div>
                    <label>
                        Минимальная цена:
                        <input
                            type="number"
                            name="minPrice"
                            value={filters.minPrice}
                            onChange={handleInputChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Максимальная цена:
                        <input
                            type="number"
                            name="maxPrice"
                            value={filters.maxPrice}
                            onChange={handleInputChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Время отправления:
                        <input
                            type="datetime-local"
                            name="departureTime"
                            value={filters.departureTime}
                            onChange={handleInputChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Время прибытия:
                        <input
                            type="datetime-local"
                            name="arrivalTime"
                            value={filters.arrivalTime}
                            onChange={handleInputChange}
                        />
                    </label>
                </div>
                <div>
                    <label>
                        Аэропорт отправления:
                        <select name="departureAirport" value={filters.departureAirport} onChange={handleInputChange}>
                            <option value="">Выберите аэропорт</option>
                            {airports.departure.map(airport => (
                                <option key={airport} value={airport}>{airport}</option>
                            ))}
                        </select>
                    </label>
                </div>
                <div>
                    <label>
                        Аэропорт прибытия:
                        <select name="arrivalAirport" value={filters.arrivalAirport} onChange={handleInputChange}>
                            <option value="">Выберите аэропорт</option>
                            {airports.arrival.map(airport => (
                                <option key={airport} value={airport}>{airport}</option>
                            ))}
                        </select>
                    </label>
                </div>
                <div>
                    <label>
                        Авиакомпания:
                        <select name="airline" value={filters.airline} onChange={handleInputChange}>
                            <option value="">Выберите авиакомпанию</option>
                            {airlines.map(airline => (
                                <option key={airline} value={airline}>{airline}</option>
                            ))}
                        </select>
                    </label>
                </div>
                <div className="sort-controls">
                    <label htmlFor="sortField">Сортировка</label>
                    <select
                        id="sortField"
                        value={sortField}
                        onChange={(e) => setSortField(e.target.value)}
                    >
                        <option value="">Выберите...</option>
                        <option value="price">Цене</option>
                        <option value="duration">Длительности</option>
                        
                    </select>

                    <button onClick={() => setSortOrder('asc')}>По возрастанию</button>
                    <button onClick={() => setSortOrder('desc')}>По убыванию</button>
                </div>
            </div>
            {isLoading && <p>Загрузка...</p>}
            {error && <p>Ошибка: {error}</p>}
            {!isLoading && !error && (
                <ul>
                    <div className='ticket button-container'>
                        <div className='ticket-field'>
                            <button className='big-button' onClick={toggleFiltersVisibility}>
                                {isFiltersVisible ? 'Скрыть фильтры' : 'Показать фильтры'}
                            </button>
                        </div>
                        <div className='ticket-field'>
                            <button onClick={() => window.location.href='/orders'}>Мои заказы</button>
                        </div>
                        
                    </div>


                    {tickets?.map(ticket => {
                        const isOrdered = checkIfTicketOrdered(ticket.id);
                        return (<div className='ticket'>
                            <li key={ticket.id}>
                                <div className='ticket-field'><strong>Авиакомпания:</strong> {ticket.airline}</div>
                                <div className='ticket-field'><strong>Отправление:</strong> {ticket.departureAirport}</div>
                                <div className='ticket-field'><strong>Прибытие:</strong> {ticket.arrivalAirport}</div>
                                <div className='ticket-field'><strong>Время отправления:</strong> {ticket.departureDatetime}</div>
                                <div className='ticket-field'><strong>Время прибытия:</strong> {ticket.arrivalDatetime}</div>
                                <div className='ticket-field'><strong>Цена:</strong> {ticket.price}</div>
                            </li>
                            <button
                                className='big-button'
                                onClick={() => handleOrderTicket(ticket.id)}
                                disabled={isOrdered}
                            >
                                {isOrdered ? 'Заказан' : 'Заказать'}
                            </button>
                        </div>

                        )
                    })}
                </ul>
            )}
            <div className='pagination'>
                <button onClick={handlePreviousPage} disabled={currentPage + 1 <= 1}>
                    Назад
                </button>
                {/* Отображение текущей страницы */}
                <span>Страница {currentPage + 1} из {totalPages}</span>
                <button onClick={handleNextPage} disabled={currentPage + 1 >= totalPages}>
                    Вперед
                </button>
            </div>
        </div>
    );
};

export default TicketList;
