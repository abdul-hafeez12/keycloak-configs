import Keycloak from "keycloak-js";

const keyclok = new Keycloak({
    url:"http://localhost:7070/",
    realm:"fe-realm",
    clientId:"fe-client"
});

export default keyclok;