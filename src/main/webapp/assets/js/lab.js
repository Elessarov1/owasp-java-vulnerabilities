function contextPath() {
  const path = window.location.pathname;
  const secondSlashIndex = path.indexOf('/', 1);

  if (secondSlashIndex === -1) {
    return '';
  }

  return path.substring(0, secondSlashIndex);
}

function setLoading(statusBadge, result) {
  statusBadge.className = 'status';
  statusBadge.textContent = 'Loading...';
  result.textContent = '';
}

function setResponseStatus(statusBadge, response) {
  if (response.ok) {
    statusBadge.className = 'status ok';
    statusBadge.textContent = `${response.status} OK`;
  } else {
    statusBadge.className = 'status error';
    statusBadge.textContent = `${response.status} Error`;
  }
}

function setRequestFailed(statusBadge, result, error) {
  statusBadge.className = 'status error';
  statusBadge.textContent = 'Request failed';
  result.textContent = error.message;
}

function formatBody(body) {
  return typeof body === 'string'
    ? body
    : JSON.stringify(body, null, 2);
}

async function readResponseBody(response) {
  const contentType = response.headers.get('content-type') || '';

  if (contentType.includes('application/json')) {
    return await response.json();
  }

  return await response.text();
}

async function sendJsonGetRequest({ path, headers = {}, requestText, statusElementId = 'status', requestLineElementId = 'requestLine', resultElementId = 'result' }) {
  const statusBadge = document.getElementById(statusElementId);
  const requestLine = document.getElementById(requestLineElementId);
  const result = document.getElementById(resultElementId);

  const url = contextPath() + path;

  requestLine.textContent = requestText || `GET ${path}`;
  setLoading(statusBadge, result);

  try {
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        ...headers
      }
    });

    const body = await readResponseBody(response);

    setResponseStatus(statusBadge, response);
    result.textContent = formatBody(body);
  } catch (error) {
    setRequestFailed(statusBadge, result, error);
  }
}

function renderLabFooter(elementId = 'labFooter') {
  const footer = document.getElementById(elementId);

  if (!footer) {
    return;
  }

  footer.innerHTML = `
        This demo is part of an educational project based on the
        <a href="https://owasp.org/www-project-top-ten/" target="_blank" rel="noopener noreferrer">
            OWASP Top 10
        </a>.
        The vulnerable examples are intentionally insecure and must not be used in production.
    `;
}