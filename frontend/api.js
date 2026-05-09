const API_URL = 'http://localhost:8080/api/v1';

// Pega o token do localStorage
function getToken() {
    return localStorage.getItem('token');
}

// Salva token e redireciona
function setToken(token) {
    localStorage.setItem('token', token);
}

// Remove token (logout)
function removeToken() {
    localStorage.removeItem('token');
    window.location.href = 'index.html';
}

// Chamada genérica à API
async function apiCall(endpoint, method = 'GET', body = null, requireAuth = true) {
    const headers = { 'Content-Type': 'application/json' };

    if (requireAuth) {
        const token = getToken();
        if (!token) {
            removeToken();
            throw new Error('Não autenticado');
        }
        headers['Authorization'] = `Bearer ${token}`;
    }

    const config = { method, headers };
    if (body) config.body = JSON.stringify(body);

    const response = await fetch(`${API_URL}${endpoint}`, config);

    if (response.status === 401) {
        removeToken();
        throw new Error('Sessão expirada');
    }

    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
        throw new Error(data?.message || 'Erro na requisição');
    }

    return data;
}

// === AUTH ===
async function register(name, lastname, email, password) {
    return apiCall('/register', 'POST', { name, lastname, email, password }, false);
}

async function authenticate(username, password) {
    return apiCall('/authenticate', 'POST', { username, password }, false);
}

// === PATIENTS ===
async function createPatient(name, document, insuranceType) {
    return apiCall('/patients', 'POST', { name, document, insuranceType });
}

async function listPatients() {
    return apiCall('/patients');
}

// === DOCTORS ===
async function createDoctor(name, specialty, license) {
    return apiCall('/doctors', 'POST', { name, specialty, license });
}

async function listDoctors() {
    return apiCall('/doctors');
}

// === PROCEDURES ===
async function createProcedure(name, cost) {
    return apiCall('/procedures', 'POST', { name, cost });
}

async function listProcedures() {
    return apiCall('/procedures');
}

// === APPOINTMENTS ===
async function createAppointment(patientId, doctorId, scheduledAt) {
    return apiCall('/appointments', 'POST', { patientId, doctorId, scheduledAt });
}

async function listAppointments() {
    return apiCall('/appointments');
}

async function addProcedureToAppointment(appointmentId, procedureId, quantity) {
    return apiCall(`/appointments/${appointmentId}/procedures`, 'POST', { procedureId, quantity });
}

async function closeAppointment(appointmentId) {
    return apiCall(`/appointments/${appointmentId}/close`, 'PATCH');
}

async function billAppointment(appointmentId) {
    return apiCall(`/appointments/${appointmentId}/bill`, 'PATCH');
}

async function calculateBill(appointmentId) {
    return apiCall(`/appointments/${appointmentId}/bill`, 'GET');
}

async function rescheduleAppointment(appointmentId, newScheduledAt) {
    return apiCall(`/appointments/${appointmentId}/reschedule`, 'PATCH', { newScheduledAt });
}

async function cancelAppointment(appointmentId, reason) {
    return apiCall(`/appointments/${appointmentId}/cancel`, 'PATCH', { reason });
}

// Helper para mostrar alertas
function showAlert(type, message) {
    const alert = document.getElementById('alert');
    alert.className = `alert ${type}`;
    alert.textContent = message;
    alert.style.display = 'block';
    setTimeout(() => { alert.style.display = 'none'; }, 4000);
}