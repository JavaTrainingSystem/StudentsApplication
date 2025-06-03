  const currentUrl = new URL(window.location.href);
  const currentPort = currentUrl.port ? `:${currentUrl.port}` : '';
  const currentContextPath = '';
  const BASE_URL = `${currentUrl.protocol}//${currentUrl.hostname}${currentPort}${currentContextPath ? '/' + currentContextPath + '/' : '/' + 'api/v1/'}`;
