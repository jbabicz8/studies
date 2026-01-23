
[AttributeUsage(AttributeTargets.Property)]
public class LogowalneAttribute : Attribute { }

// Atrybut logowalne zdefiniowałem jako własny znacznik metadanych, ograniczony do właściwości. 
// WYkorzystany z mechanizmem refleksji, aby identyfikować pola modelu, które mają podlegać procesom diagnostycznym.