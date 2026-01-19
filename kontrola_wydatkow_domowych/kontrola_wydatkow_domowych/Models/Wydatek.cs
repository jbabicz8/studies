namespace kontrola_wydatkow_domowych.Models
{
    public class Wydatek : ElementBudzetu // klasa wydatek dziedziczy po klasie ElementBudzetu
    {
        public DateTime Data { get; set; } // data wydatku
        public string? Opis { get; set; } // opcjonalny opis wydatku
        public int KategoriaId { get; set; } // identyfikator kategorii wydatku

        public decimal Kwota 
        {
            get => Wartosc; // wlasciwosc kwota odwoluje sie do wartosci z klasy bazowej podpina sie pod pole wartosc z elementbudzetu.
            set => Wartosc = value; // jesli ustawiamy kwote, to automatycznie aktualizujac pole wartosc.
        }
    }
}