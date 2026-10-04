ENGLISH STUDIES FLDM — SIGNATURE RELEASE

Le projet est configuré pour signer automatiquement la variante release.

FICHIERS DE SIGNATURE
- keystore/englishstudies-upload.jks : clé privée d'importation (à conserver en lieu sûr)
- keystore/englishstudies-upload-certificate.pem : certificat public
- keystore.properties : paramètres Gradle locaux (NE PAS publier)

Alias : englishstudies_upload
Algorithme : RSA 4096 bits / SHA256withRSA
Empreinte SHA-256 du certificat :
B3:C0:38:D3:7E:A1:E2:C5:BE:C9:5C:D3:7A:AB:B4:D1:65:20:AE:1D:29:A8:FA:38:89:3C:22:34:39:A8:AB:F1

MOT DE PASSE
Le mot de passe est présent dans keystore.properties.
Conserve ensemble le keystore et ce fichier de propriétés. Ne les publie jamais sur GitHub.

GÉNÉRER UN APK RELEASE SIGNÉ
Dans Android Studio, sélectionne la variante "release", puis construis l'APK.
Avec le terminal Windows :
    gradlew.bat assembleRelease

Sortie attendue :
    app\build\outputs\apk\release\app-release.apk

GÉNÉRER LE AAB POUR GOOGLE PLAY
    gradlew.bat bundleRelease

Sortie attendue :
    app\build\outputs\bundle\release\app-release.aab

IMPORTANT
Cette clé est une clé d'importation locale.

La configuration Gradle ne dépend pas de java.util.Properties : elle lit directement keystore.properties. Pour une nouvelle application Google Play,
utilise Play App Signing : Google gère ensuite la clé de signature de distribution.
Pour une application déjà installée avec une autre signature (par exemple une build debug
ou une version Play), un APK signé avec cette nouvelle clé ne pourra pas remplacer cette
installation directement : il faudra utiliser le circuit de mise à jour approprié ou
désinstaller l'ancienne version.

Version configurée :
    versionCode = 2
    versionName = 1.1
