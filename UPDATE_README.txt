EnglishStudiesFLDM — Mise à jour 1.2

Modifications incluses :
1. S6 > Literary & Cultural Studies > Novel 2 ouvre directement Robinson Crusoe.
2. Retour depuis Robinson Crusoe vers S6 – Literary & Cultural Studies.
3. Robinson Crusoe : texte complet de l’EPUB fourni, dans RobinsonCrusoeData.kt.
4. Novel 1 : clic sur un mot = prononciation + traduction française + arabe.
5. Novel 2 / Robinson Crusoe : clic sur un mot = prononciation + traduction française + arabe.
6. Oliver Twist : écran de lecture mis à jour avec la version de travail présente dans le dossier.
7. versionCode = 3 ; versionName = 1.2.

Pour générer l’APK signé depuis Android Studio :
- Ouvrir ce projet.
- Attendre la synchronisation Gradle.
- Build > Clean Project.
- Build > Rebuild Project.
- Build > Generate App Bundles or APKs > Generate APKs.

Pour produire le release signé avec la configuration existante, utiliser :
  gradlew.bat assembleRelease

APK attendu :
  app/build/outputs/apk/release/app-release.apk

Ne pas supprimer keystore.properties ni le dossier keystore si vous souhaitez conserver la signature de mise à jour.
