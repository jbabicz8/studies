namespace kontrola_wydatkow_domowych.Services
{
    // WŁASNE WYJĄTKI
    public class WalidacjaBudzetuException : Exception // klasa dziedziczy po standardowej klasie Exception, korzysta z cech standradowych wyjątków w C#
    {
        public WalidacjaBudzetuException(string message) : base(message) { } // konstruktor przyjmujący komunikat o błędzie i przekazujący go do konstruktora klasy bazowej
    }
}
// Ta klasa dotyczy wyjątków zasad biznesowych w kontekście budżetu domowego, takich jak próba dodania wydatku z ujemną kwotą.
