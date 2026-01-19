using System.Text.Json;
using kontrola_wydatkow_domowych.Models;

namespace kontrola_wydatkow_domowych.Services
{
    // implementacja interfejsu generycznego
    public class RepozytoriumJSON : IRepozytorium<BudzetDane>
    {
        private readonly string _sciezkaPliku = "budzet.json"; // sciezka do pliku z danymi JSON

        public void Zapisz(BudzetDane dane)
        {
            // serializacja, zamienia obiekt dane na format JSON
            var json = JsonSerializer.Serialize(dane, new JsonSerializerOptions { WriteIndented = true });
            File.WriteAllText(_sciezkaPliku, json); // zapisuje do pliku
        }

        public BudzetDane? Wczytaj()
        {
            // deserializacja, zamiana tekstu z pliku z powrotem na obiekty w programie.
            if (!File.Exists(_sciezkaPliku)) return new BudzetDane(); // jeśli plik nie istnieje, zwraca nowy obiekt BudzetDane
            string jsonString = File.ReadAllText(_sciezkaPliku); // odczytuje zawartość pliku
            return JsonSerializer.Deserialize<BudzetDane>(jsonString); // deserializuje do obiektu BudzetDane
        }
    }

    // Pomocnicza klasa do serializacji (enkapsulacja struktury danych)
    public class BudzetDane // zamiast zapisywać osobno pakujemy wszystko w jednym obiekcie
    {
        public List<Wydatek> Wydatki { get; set; } = new();
        public List<Kategoria> Kategorie { get; set; } = new();
        public decimal LimitOgolny { get; set; }
    }
}