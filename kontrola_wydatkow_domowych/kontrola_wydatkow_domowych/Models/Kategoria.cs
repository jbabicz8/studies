namespace kontrola_wydatkow_domowych.Models
{
    // klasa dziedziczy czesc pól z klasy ElementBudzetu
    public class Kategoria : ElementBudzetu
    {
        public string Nazwa { get; set; } = string.Empty; // nazwa kategorii

        public decimal LimitMiesieczny // podobnie jak w klasie Wydatek, wlasciwosc limitmiesieczny odwoluje sie do wartosci i aktualizuje pole wartosc z klasy bazowej.
        {
            get => Wartosc;
            set => Wartosc = value;
        }
    }
}