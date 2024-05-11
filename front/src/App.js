import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import TicketList from './TicketList';
import MyOrders from './MyOrders';

import './App.css';


function App() {
  return (
    <Router>
      <div className="app-container">
        <header>
          {/* Навигационная панель и другие элементы header */}
        </header>
        <main>
          <Routes>
            <Route exact path="/" element={<TicketList />}></Route>
            <Route exact path="/orders" element={<MyOrders />}></Route>
          </Routes>
        </main>
        <footer>
          {/* Копирайт и другая информация */}
        </footer>
      </div>
    </Router>
  );
}

export default App;
