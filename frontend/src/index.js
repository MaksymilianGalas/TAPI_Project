import React from 'react';
import ReactDOM from 'react-dom/client';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import keycloak from './keycloak';
import App from './App';
import './index.css';

const root = ReactDOM.createRoot(document.getElementById('root'));

const eventLogger = (event, error) => {
    console.log('Keycloak event:', event, error);
};

const tokenLogger = (tokens) => {
    console.log('Keycloak tokens:', tokens);
    if (tokens.token) {
        localStorage.setItem('token', tokens.token);
    }
};

root.render(
    <React.StrictMode>
        <ReactKeycloakProvider
            authClient={keycloak}
            initOptions={{
                onLoad: 'check-sso',
                silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
                pkceMethod: 'S256',
            }}
            onEvent={eventLogger}
            onTokens={tokenLogger}
        >
            <App />
        </ReactKeycloakProvider>
    </React.StrictMode>
);
