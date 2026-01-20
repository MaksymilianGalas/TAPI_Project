import React from 'react';
import { useKeycloak } from '@react-keycloak/web';

function Home() {
    const { keycloak } = useKeycloak();

    return (
        <div className="welcome-container">
            <h1>Welcome to Microservices Application</h1>
            <p>Hello, {keycloak.tokenParsed?.preferred_username}!</p>
            <p>You are successfully authenticated.</p>

            <div className="grid" style={{ marginTop: '3rem' }}>
                <div className="stat-card">
                    <h3>3</h3>
                    <p>Microservices</p>
                </div>
                <div className="stat-card">
                    <h3>API</h3>
                    <p>Gateway</p>
                </div>
                <div className="stat-card">
                    <h3>JWT</h3>
                    <p>Security</p>
                </div>
            </div>

            <div className="card" style={{ marginTop: '2rem' }}>
                <h2>System Features</h2>
                <ul style={{ lineHeight: '2', fontSize: '1.1rem' }}>
                    <li>User Management Service</li>
                    <li>Order Management Service</li>
                    <li>Document Generation Service (PDF & Excel)</li>
                    <li>API Gateway with JWT Validation</li>
                    <li>Keycloak Authentication</li>
                    <li>Database per Service (MongoDB)</li>
                </ul>
            </div>
        </div>
    );
}

export default Home;
