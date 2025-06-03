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