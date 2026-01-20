import Keycloak from 'keycloak-js';

const keycloakConfig = {
    url: process.env.REACT_APP_KEYCLOAK_URL || 'http://localhost:8180',
    realm: process.env.REACT_APP_KEYCLOAK_REALM || 'microservices',
    clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID || 'frontend-client',
};

const keycloak = new Keycloak(keycloakConfig);

export default keycloak;
