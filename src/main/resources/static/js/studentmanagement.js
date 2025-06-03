  const currentUrl = new URL(window.location.href);
  const currentPort = currentUrl.port ? `:${currentUrl.port}` : '';
  const currentContextPath = 'student-management';
  const BASE_URL = `${currentUrl.protocol}//${currentUrl.hostname}${currentPort}/${currentContextPath}/api/v1/`;