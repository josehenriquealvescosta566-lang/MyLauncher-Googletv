# Meu Launcher (Android TV / Google TV)

Launcher simples: grade com os apps instalados, sem propaganda, cores Material You.

## Compilar
- Android Studio: abra a pasta e rode "Build > Build APK".
- Sem computador: envie a pasta para um repositório no GitHub; a aba Actions gera o APK (build.yml).

## Instalar na TV (ADB sem fio)
    adb connect IP_DA_TV:PORTA
    adb install -r app-debug.apk

Depois abra "Meu Launcher" na lista de apps da TV.
Não altera nada do sistema. Para desinstalar: adb uninstall com.meulauncher.tv
