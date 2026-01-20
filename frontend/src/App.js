import React from 'react';
import { useKeycloak } from '@react-keycloak/web';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './index.css';
import Home from './components/Home';
import UserManagement from './components/UserManagement';
import OrderManagement from './components/OrderManagement';
import DocumentGeneration from './components/DocumentGeneration';

function App() {
    const { keycloak, initialized } = useKeycloak();

    if (!initialized) {
        return <div className="loading">Loading...</div>;
    }

    if (!keycloak.authenticated) {
        return (
            <div className="welcome-container">
                <h1>Welcome to Microservices Application</h1>
                <p>Please login to continue</p>
                <button className="btn" onClick={() => keycloak.login()}>
                    Login
                </button>
            </div>
        );
    }

    return (
        <Router>
            <div className="app-container">
                <nav className="navbar">
                    <h1>Microservices App</h1>
                    <div style={{ display: 'flex', gap: '1.5rem', alignItems: 'center' }}>
                        <Link to="/">Home</Link>
                        <Link to="/users">Users</Link>
                        <Link to="/orders">Orders</Link>
                        <Link to="/documents">Documents</Link>
                        <span style={{ color: '#667eea', fontWeight: 'bold' }}>
                            {keycloak.tokenParsed?.preferred_username}
                        </span>
                        <button onClick={() => keycloak.logout()}>Logout</button>
                    </div>
                </nav>

                <div className="main-content">
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/users" element={<UserManagement />} />
                        <Route path="/orders" element={<OrderManagement />} />
                        <Route path="/documents" element={<DocumentGeneration />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
