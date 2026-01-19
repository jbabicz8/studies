using kontrola_wydatkow_domowych.Models;
using System.Reflection;

namespace kontrola_wydatkow_domowych.Services
{
    public class BudzetDomowy // GŁÓWNA KLASA BUDŻETU DOMOWEGO
    {
        private List<Wydatek> _wydatki = new(); // kolekcja wydatków
        private List<Kategoria> _kategorie = new(); // kolekcja kategorii

        public List<Wydatek> Wydatki => _wydatki; // właściwość tylko do odczytu zwracająca listę wydatków
        public List<Kategoria> Kategorie => _kategorie; // właściwość tylko do odczytu zwracająca listę kategorii
        public decimal LimitOgolny { get; set; } // właściwość przechowująca ogólny limit budżetu

        // DELEGACJE I ZDARZENIA
        public delegate void PowiadomienieHandler(string tresc); // definicja delegata dla powiadomień
        public event PowiadomienieHandler? OnPowiadomienie; // zdarzenie wywoływane przy przekroczeniu budżetu

        private readonly IRepozytorium<BudzetDane> _repo = new RepozytoriumJSON(); // repozytorium do zapisu/odczytu danych

        public BudzetDomowy() // konstruktor klasy
        {
            var dane = _repo.Wczytaj(); // wczytuje dane z repozytorium
            if (dane != null) // jeśli dane istnieją, inicjalizuje kolekcje i limit
            {
                _wydatki = dane.Wydatki;
                _kategorie = dane.Kategorie;
                LimitOgolny = dane.LimitOgolny;
            }
        }

        public void DodajWydatek(Wydatek w) // metoda dodająca wydatek
        {
            // WYJĄTKI - walidacja
            if (w.Kwota <= 0) throw new WalidacjaBudzetuException("Kwota musi być dodatnia!"); // sprawdza czy kwota jest dodatnia

            w.Id = _wydatki.Count > 0 ? _wydatki.Max(x => x.Id) + 1 : 1; // ustawia unikalne Id wydatku
            _wydatki.Add(w);
            ZapiszStan();
            SprawdzBudzet(w.KategoriaId);
        }

        public void UsunWydatek(int id) // metoda usuwająca wydatek
        {
            _wydatki.RemoveAll(x => x.Id == id); // usuwa wydatek o podanym Id
            ZapiszStan();
        }

        public void EdytujWydatek(Wydatek uparty) // metoda edytująca wydatek
        {
            var istniejący = _wydatki.FirstOrDefault(x => x.Id == uparty.Id); // znajduje istniejący wydatek
            if (istniejący != null) //
            {
                istniejący.Kwota = uparty.Kwota;
                istniejący.Opis = uparty.Opis;
                istniejący.Data = uparty.Data;
                istniejący.KategoriaId = uparty.KategoriaId;
                ZapiszStan();
            }
        }

        public void DodajKategorie(Kategoria k) // metoda dodająca kategorię
        {
            k.Id = _kategorie.Count > 0 ? _kategorie.Max(x => x.Id) + 1 : 1; // ustawia unikalne Id kategorii
            _kategorie.Add(k);
            ZapiszStan();
        }

        public void UsunKategorie(int id) // metoda usuwająca kategorię
        {
            var kat = _kategorie.FirstOrDefault(x => x.Id == id); // znajduje kategorię o podanym Id
            if (kat != null)
            {
                _kategorie.Remove(kat);
                _wydatki.Where(x => x.KategoriaId == id).ToList().ForEach(w => w.KategoriaId = 0); // usuwa powiązania wydatków z tą kategorią
                ZapiszStan();
            }
        }

        public void EdytujKategorie(Kategoria zmieniona) // metoda edytująca kategorię
        {
            var stara = _kategorie.FirstOrDefault(k => k.Id == zmieniona.Id); // znajduje istniejącą kategorię
            if (stara != null)
            {
                stara.Nazwa = zmieniona.Nazwa; // aktualizuje nazwę kategorii
                stara.LimitMiesieczny = zmieniona.LimitMiesieczny; // aktualizuje limit miesięczny kategorii
                ZapiszStan();
            }
        }

        public void ZapiszStan() // metoda zapisująca stan budżetu
        {
            _repo.Zapisz(new BudzetDane // tworzy obiekt BudzetDane i zapisuje go w repozytorium
            {
                Wydatki = _wydatki,
                Kategorie = _kategorie,
                LimitOgolny = LimitOgolny
            });
        }

        private void SprawdzBudzet(int kategoriaId) // metoda sprawdzająca przekroczenie budżetu
        {
            var kategoria = _kategorie.FirstOrDefault(k => k.Id == kategoriaId); // znajduje kategorię o podanym Id
            if (kategoria == null || kategoria.LimitMiesieczny <= 0) return; // jeśli kategoria nie istnieje lub limit jest nieustawiony, kończy działanie

            var suma = _wydatki // oblicza sumę wydatków w danej kategorii za bieżący miesiąc
                .Where(w => w.KategoriaId == kategoriaId && w.Data.Month == DateTime.Now.Month) // sumuje wydatki w danej kategorii za bieżący miesiąc
                .Sum(w => w.Kwota); // oblicza sumę wydatków

            if (suma > kategoria.LimitMiesieczny)
            {
                // WYWOŁANIE ZDARZENIA
                OnPowiadomienie?.Invoke($"Przekroczono budżet: {kategoria.Nazwa}");
            }
        }

        // REFLEKSJA - przykład użycia do diagnostyki
        public string PobierzTypyKolekcji() // metoda zwracająca typy kolekcji używanych w klasie
        {
            return $"Wydatki: {_wydatki.GetType().Name}, Kategorie: {_kategorie.GetType().Name}"; // zwraca nazwy typów kolekcji
        }
    }
}