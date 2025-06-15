  const currentUrl = new URL(window.location.href);
  const currentPort = currentUrl.port ? `:${currentUrl.port}` : '';
  const currentContextPath = '';
  const BASE_URL = `${currentUrl.protocol}//${currentUrl.hostname}${currentPort}${currentContextPath ? '/' + currentContextPath + '/' : '/' + 'api/v1/'}`;

  const tokenVar = "accessToken";

  function setTokenDetails(token) {
    window.localStorage.setItem(tokenVar, 'Bearer ' + token);
  }

  function getToken() {
    return window.localStorage.getItem(tokenVar);
  }

  function removeToken() {
    window.localStorage.removeItem(tokenVar);
  }

  function handleUnauthorized() {
    removeToken();
    window.location.href = "login";
  }


  function navigateTo(section) {
       showLoader("Loading...");
      window.location.href = section;
    }


    function logout(){
      removeToken();
          window.location.href = "login";
    }


function getSubFromJWT() {
var token = getToken();
    const payloadBase64 = token.split('.')[1]; // Get the payload part
    const decodedPayload = JSON.parse(atob(payloadBase64)); // Decode from Base64
    return decodedPayload.sub; // Return the 'sub' claim
}
