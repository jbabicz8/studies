let globalneKategorie = [];
let mojWykres = null;
let limitOgolny = 0;

// Inicjalizacja po załadowaniu DOM
document.addEventListener('DOMContentLoaded', async () => {
    inicjalizujFiltry();
    await zaladujKategorie();
    await odswiezWydatki();
});

// Funkcja synchronizująca dwa zestawy filtrów (Panel i Kalendarz)
function synchronizujFiltry(zrodlo, celId) {
    document.getElementById(celId).value = zrodlo.value;
}

// --- FILTROWANIE ---
function inicjalizujFiltry() {
    const ids = [['filtrMiesiac', 'filtrMiesiacKal'], ['filtrRok', 'filtrRokKal']];
    const teraz = new Date();
    const nazwyMiesiecy = ["Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"];

    ids[0].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.innerHTML = nazwyMiesiecy.map((m, i) => `<option value="${i + 1}" ${i === teraz.getMonth() ? 'selected' : ''}>${m}</option>`).join(''); // Miesiące są indeksowane od 0, więc dodajemy 1
    });

    const opcjeLat = [];
    for (let i = -2; i < 3; i++) opcjeLat.push(teraz.getFullYear() + i); // Zakres lat: od 2 lata wstecz do 2 lata wprzód
    ids[1].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.innerHTML = opcjeLat.map(r => `<option value="${r}" ${r === teraz.getFullYear() ? 'selected' : ''}>${r}</option>`).join(''); // Ustawienie bieżącego roku jako domyślnego
    });
}

// --- NAWIGACJA MIĘDZY SEKCJAMI ---
function pokazSekcje(id) {
    document.getElementById('podgladGlowny').classList.toggle('hidden', id !== 'glowna');
    document.getElementById('zarzadzanieKategoriami').classList.toggle('hidden', id !== 'kategorie');
    document.getElementById('sekcjaRaportow').classList.toggle('hidden', id !== 'raporty');
    document.getElementById('sekcjaKalendarza').classList.toggle('hidden', id !== 'kalendarz');

    if (id === 'glowna') odswiezWydatki();
    else if (id === 'kategorie') odswiezWidokKategorii();
    else if (id === 'raporty') generujRaporty();
    else if (id === 'kalendarz') generujKalendarz();
}

// --- WYDATKI I STATYSTYKI ---
async function odswiezWydatki() {
    const resLimit = await fetch('/api/Wydatki/Limit');
    limitOgolny = await resLimit.json();

    const res = await fetch('/api/Wydatki');
    let wydatki = await res.json();

    const wybranyMiesiac = parseInt(document.getElementById('filtrMiesiac').value);
    const wybranyRok = parseInt(document.getElementById('filtrRok').value);

    // Filtrowanie danych dla aktualnie wybranego okresu
    const filtrowaneWydatki = wydatki.filter(w => {
        const d = new Date(w.data);
        return (d.getMonth() + 1) === wybranyMiesiac && d.getFullYear() === wybranyRok;
    });

    const lista = document.getElementById('listaWydatkow');
    let suma = 0;

    // Generowanie wierszy tabeli wydatków
    lista.innerHTML = filtrowaneWydatki.map(w => {
        suma += w.kwota;
        const kat = globalneKategorie.find(k => k.id === w.kategoriaId); // Znajdź kategorię wydatku
        const nazwaKat = kat ? kat.nazwa : "Brak / Usunięta"; // Obsługa przypadku braku kategorii

        return `<tr class="border-t hover:bg-gray-50 transition-colors">
            <td class="p-4 text-gray-600 text-sm">${new Date(w.data).toLocaleDateString()}</td>
            <td class="p-4 font-bold text-gray-900">${w.kwota.toFixed(2)} PLN</td>
            <td class="p-4"><span class="bg-blue-100 text-blue-700 px-2 py-1 rounded text-[10px] font-bold uppercase">${nazwaKat}</span></td>
            <td class="p-4 text-gray-500 text-sm italic">${w.opis || '-'}</td>
            <td class="p-4 text-center space-x-3">
                <button onclick="przygotujEdycjeWydatku(${w.id})" class="text-blue-600 hover:text-blue-800 text-xs font-bold">EDYTUJ</button>
                <button onclick="usunWydatek(${w.id})" class="text-red-500 hover:text-red-700 text-xs font-bold">USUŃ</button>
            </td>
        </tr>`;
    }).join('');

    // UI Podsumowania
    const zostalo = limitOgolny - suma;
    const procent = limitOgolny > 0 ? (suma / limitOgolny) * 100 : 0;

    document.getElementById('sumaWydatkow').innerText = `${suma.toFixed(2)} PLN`;
    document.getElementById('pokazLimit').innerText = `${limitOgolny.toFixed(2)} PLN`;
    document.getElementById('pokazZostalo').innerText = `${zostalo.toFixed(2)} PLN`;
    document.getElementById('pokazZostalo').style.color = zostalo < 0 ? "#ef4444" : "#16a34a";

    const alertElem = document.getElementById('alertBudzetu');
    const pasek = document.getElementById('pasekPostepu');
    const procentElem = document.getElementById('procentBudzetu');

    if (limitOgolny > 0 && suma > limitOgolny) {
        alertElem.classList.remove('hidden');
        pasek.style.backgroundColor = "#ef4444";
        procentElem.style.color = "#ef4444";
    } else {
        alertElem.classList.add('hidden');
        pasek.style.backgroundColor = "#2563eb";
        procentElem.style.color = "#2563eb";
    }

    procentElem.innerText = `${procent.toFixed(1)}%`;
    pasek.style.width = `${Math.min(procent, 100)}%`;

    aktualizujWykres(filtrowaneWydatki);
}

// --- WYKRES ---
function aktualizujWykres(wydatki) {
    const ctx = document.getElementById('wykresWydatkow').getContext('2d');
    const podzial = {};
    wydatki.forEach(w => {
        const kat = globalneKategorie.find(k => k.id === w.kategoriaId);
        const nazwa = kat ? kat.nazwa : "Brak / Inne";
        podzial[nazwa] = (podzial[nazwa] || 0) + w.kwota;
    });

    if (mojWykres) mojWykres.destroy();
    mojWykres = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: Object.keys(podzial),
            datasets: [{
                data: Object.values(podzial),
                backgroundColor: ['#2563eb', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899', '#06b6d4'],
                borderWidth: 2,
                borderColor: '#ffffff'
            }]
        },
        options: {
            maintainAspectRatio: false,
            plugins: { legend: { position: 'bottom', labels: { boxWidth: 10, font: { size: 9 } } } }
        }
    });
}

// --- KALENDARZ ---
async function generujKalendarz() {
    const res = await fetch('/api/Wydatki');
    const wydatki = await res.json();

    // Pobieramy wartości z filtrów kalendarza
    const sMiesiac = parseInt(document.getElementById('filtrMiesiacKal').value);
    const sRok = parseInt(document.getElementById('filtrRokKal').value);

    const dataRef = new Date(sRok, sMiesiac - 1);
    document.getElementById('etykietaMiesiaca').innerText = dataRef.toLocaleString('pl-PL', { month: 'long', year: 'numeric' });

    const pierwszyDzien = new Date(sRok, sMiesiac - 1, 1).getDay();
    const offset = pierwszyDzien === 0 ? 6 : pierwszyDzien - 1;
    const dniWMiesiacu = new Date(sRok, sMiesiac, 0).getDate();

    const wydatkiDni = {};
    let sumaMiesiac = 0;
    let licznikTransakcji = 0;

    wydatki.forEach(w => {
        const d = new Date(w.data);
        if (d.getMonth() + 1 === sMiesiac && d.getFullYear() === sRok) {
            const dzien = d.getDate();
            wydatkiDni[dzien] = (wydatkiDni[dzien] || 0) + w.kwota;
            sumaMiesiac += w.kwota;
            licznikTransakcji++;
        }
    });

    // Aktualizacja podsumowania na dole kalendarza
    document.getElementById('sumaKalendarz').innerText = `${sumaMiesiac.toFixed(2)} PLN`;
    document.getElementById('iloscWydatkowKal').innerText = licznikTransakcji;

    const kontener = document.getElementById('dniKalendarza');
    let html = '';

    for (let i = 0; i < offset; i++) html += `<div class="bg-gray-50/50 p-4 min-h-[85px]"></div>`;

    for (let d = 1; d <= dniWMiesiacu; d++) {
        const suma = wydatkiDni[d] || 0;
        const czyDzis = new Date().getDate() === d && new Date().getMonth() + 1 === sMiesiac && new Date().getFullYear() === sRok;

        html += `
            <div class="bg-white p-2 min-h-[85px] border-r border-b flex flex-col justify-between hover:bg-blue-50 transition-all ${czyDzis ? 'ring-2 ring-blue-400 ring-inset' : ''}">
                <span class="text-[10px] font-bold ${czyDzis ? 'text-blue-600' : 'text-gray-400'}">${d}</span>
                <span class="text-[11px] font-black ${suma > 0 ? 'text-gray-900' : 'text-gray-200'} text-right">
                    ${suma > 0 ? suma.toFixed(2) : '0.00'}
                </span>
            </div>`;
    }
    kontener.innerHTML = html;
}

// --- RAPORTY ---
async function generujRaporty() {
    const res = await fetch('/api/Wydatki');
    const wydatki = await res.json();
    const kontener = document.getElementById('listaRaportowMiesiecznych');
    const raporty = {};

    wydatki.forEach(w => {
        const d = new Date(w.data);
        const klucz = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`;
        raporty[klucz] = (raporty[klucz] || 0) + w.kwota;
    });

    const klucze = Object.keys(raporty).sort().reverse();
    kontener.innerHTML = klucze.map(klucz => {
        const [rok, mies] = klucz.split('-');
        const nazwaMiesiaca = new Date(rok, mies - 1).toLocaleString('pl-PL', { month: 'long' });
        const suma = raporty[klucz];
        return `<div class="border rounded-xl p-5 shadow-sm bg-white hover:border-blue-300 transition-all">
                    <div class="text-[10px] font-bold text-gray-400 uppercase tracking-widest">${rok}</div>
                    <div class="text-xl font-black text-gray-800 mb-1 capitalize">${nazwaMiesiaca}</div>
                    <div class="text-2xl font-bold text-blue-600 mb-4">${suma.toFixed(2)} PLN</div>
                    <button onclick="eksportujMiesiac('${klucz}')" class="w-full bg-gray-50 text-gray-600 text-[10px] font-bold py-2 rounded hover:bg-blue-600 hover:text-white transition-all">EKSPORTUJ JSON</button>
                </div>`;
    }).join('');
}

// --- EKSPORT ---
async function eksportujMiesiac(klucz) {
    const [rok, mies] = klucz.split('-').map(Number);
    const res = await fetch('/api/Wydatki');
    const wydatki = await res.json();

    const filtrowane = wydatki.filter(w => {
        const d = new Date(w.data);
        return d.getFullYear() === rok && (d.getMonth() + 1) === mies;
    }).map(w => {
        const kat = globalneKategorie.find(k => k.id === w.kategoriaId);
        return { data: new Date(w.data).toLocaleDateString(), kwota: w.kwota, kategoria: kat ? kat.nazwa : "Brak", opis: w.opis };
    });

    const blob = new Blob([JSON.stringify(filtrowane, null, 2)], { type: 'application/json' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = `raport_${klucz}.json`;
    a.click();
}

// --- POMOCNICZE (MODALE / CRUD) ---
async function zaladujKategorie() {
    const res = await fetch('/api/Wydatki/Kategorie');
    globalneKategorie = await res.json();
    const select = document.getElementById('formCategory');
    if (select) select.innerHTML = globalneKategorie.map(k => `<option value="${k.id}">${k.nazwa}</option>`).join('');
}

// Dodawanie wydatku
async function dodajWydatek() {
    const dane = {
        kwota: parseFloat(document.getElementById('formAmount').value),
        data: document.getElementById('formDate').value,
        kategoriaId: parseInt(document.getElementById('formCategory').value),
        opis: document.getElementById('formDesc').value
    };
    await fetch('/api/Wydatki', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dane) });
    zamknijModale();
    odswiezWydatki();
}

// Usuwanie wydatku
async function usunWydatek(id) {
    if (confirm("Usunąć?")) { await fetch(`/api/Wydatki/${id}`, { method: 'DELETE' }); odswiezWydatki(); }
}

// Przygotowanie modalu do edycji wydatku
async function przygotujEdycjeWydatku(id) {
    const res = await fetch('/api/Wydatki');
    const wydatki = await res.json();
    const w = wydatki.find(x => x.id === id);
    if (w) {
        document.getElementById('formAmount').value = w.kwota;
        document.getElementById('formDate').value = w.data.split('T')[0];
        document.getElementById('formCategory').value = w.kategoriaId;
        document.getElementById('formDesc').value = w.opis;
        const stopka = document.querySelector('#modalWydatek .flex.gap-2');
        stopka.innerHTML = `<button onclick="zamknijModale()" class="flex-1 bg-gray-200 p-2 rounded">Anuluj</button>
                            <button onclick="zatwierdzEdycje(${id})" class="flex-1 bg-orange-500 text-white p-2 rounded">Zapisz</button>`;
        otworzModalWydatek();
    }
}

// Zatwierdzenie edycji wydatku
async function zatwierdzEdycje(id) {
    const dane = { kwota: parseFloat(document.getElementById('formAmount').value), data: document.getElementById('formDate').value, kategoriaId: parseInt(document.getElementById('formCategory').value), opis: document.getElementById('formDesc').value };
    await fetch(`/api/Wydatki/${id}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dane) });
    zamknijModale();
    odswiezWydatki();
}

// Zarządzanie kategoriami
async function odswiezWidokKategorii() {
    const tabela = document.getElementById('listaKategoriiUI');
    tabela.innerHTML = globalneKategorie.map(k => `<tr class="border-t"><td class="p-4 font-bold">${k.nazwa}</td><td class="p-4">${k.limitMiesieczny.toFixed(2)} PLN</td><td class="p-4 text-center"><button onclick="usunKategorie(${k.id})" class="text-red-500 font-bold">USUŃ</button></td></tr>`).join('');
}

// Dodawanie kategorii
async function dodajKategorie() {
    const dane = { nazwa: document.getElementById('katName').value, limitMiesieczny: parseFloat(document.getElementById('katLimit').value) || 0 };
    await fetch('/api/Wydatki/Kategorie', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dane) });
    await zaladujKategorie();
    zamknijModale();
    odswiezWidokKategorii();
}

// Usuwanie kategorii
async function usunKategorie(id) {
    if (confirm("Usunąć kategorię?")) { await fetch(`/api/Wydatki/Kategorie/${id}`, { method: 'DELETE' }); await zaladujKategorie(); odswiezWidokKategorii(); }
}

// Zapis limitu ogólnego
async function zapiszLimitOgolny() {
    const limit = parseFloat(document.getElementById('formLimitOgolny').value) || 0;
    await fetch('/api/Wydatki/Limit', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(limit) });
    zamknijModale();
    odswiezWydatki();
}

// --- MODALE ---
function otworzModalWydatek() { document.getElementById('modalWydatek').classList.replace('hidden', 'flex'); }
function otworzModalKategorii() { document.getElementById('modalKategoria').classList.replace('hidden', 'flex'); }
function otworzModalLimitu() { document.getElementById('modalLimitu').classList.replace('hidden', 'flex'); }
function zamknijModale() {
    document.getElementById('modalWydatek').classList.replace('flex', 'hidden');
    document.getElementById('modalKategoria').classList.replace('flex', 'hidden');
    document.getElementById('modalLimitu').classList.replace('flex', 'hidden');
    resetujModalWydatku();
}
// Resetowanie modalu dodawania wydatku do stanu początkowego
function resetujModalWydatku() {
    const stopka = document.querySelector('#modalWydatek .flex.gap-2');
    if (stopka) stopka.innerHTML = `<button onclick="zamknijModale()" class="flex-1 bg-gray-200 p-2 rounded">Anuluj</button><button onclick="dodajWydatek()" class="flex-1 bg-blue-600 text-white p-2 rounded">Zapisz</button>`;
}