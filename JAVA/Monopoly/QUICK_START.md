# ⚡ Guía Rápida de Ejecución - Monopoly

## ✅ Opción 1: Desde Terminal (Recomendado)

```powershell
cd C:\Users\mercu\Videos\JAVA_TRADE\JAVA\Monopoly
java -cp bin com.monopoly.Main
```

## ✅ Opción 2: Con Compilación

```powershell
cd C:\Users\mercu\Videos\JAVA_TRADE\JAVA\Monopoly

# Compilar
javac -d bin -sourcepath "src/main/java" $(Get-ChildItem -Path "src/main/java" -Recurse -Include "*.java" | ForEach-Object { $_.FullName })

# Ejecutar
java -cp bin com.monopoly.Main
```

## ✅ Opción 3: Desde VS Code (si lo tienes abierto en esta carpeta)

### Abrir VS Code en la carpeta Monopoly:
```powershell
cd C:\Users\mercu\Videos\JAVA_TRADE\JAVA\Monopoly
code .
```

Luego:
1. Presiona `Ctrl+Shift+D` (Debug)
2. Selecciona "Launch Monopoly"
3. O presiona `Ctrl+Shift+B` para ejecutar la tarea de compilación/ejecución

## 🚨 Problema Actual

**VS Code está abierto en:** `C:\Users\mercu\Videos\JAVA_TRADE`  
**Debería estar en:** `C:\Users\mercu\Videos\JAVA_TRADE\JAVA\Monopoly`

### Solución
Abre una nueva ventana de VS Code:
```powershell
code "C:\Users\mercu\Videos\JAVA_TRADE\JAVA\Monopoly"
```

---

**Nota:** Los archivos `.vscode/settings.json`, `.vscode/launch.json` y `.vscode/tasks.json` están configurados correctamente para ejecutar desde la carpeta Monopoly.
