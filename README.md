# Sound Processing
*toute proposition de nom acceptée :3*

## Analyze

Module destiné à l'analyse d'enregistrement.  
Extrait les premiers coefficients de fourier + coefficients mfc pour être utilisés comme feature.  
Le vecteur de caractéristiques contiendra la moyenne et la variance de chaque fréquence sur l'ensemble des Frames de l'enregistrement.
- Utilisation :
  - générer un fichier d'enregistrements à l'aide du module **sound**
  - `sbt analyze/run` va lire le fichier généré, puis convertir chaque enregistrement en features
  - une ligne de ce fichier correspond aux features d'un enregistrement
  
#### Vecteur de caractéristiques

Le vecteur de sortie sera composé comme suit : 

répartition : [-moyennes des FFTC-|-moyenne des MFCC-|-std des FFTC-|-std des MFCC-]  
taille totale : 1024

#### Paramètres

##### AnalyzeConfiguration 

- **normalize** (Bool) (optional) : si true, normalise le vecteur de sortie

##### LocalConfiguration

- **input**:
  - **file** (Path) : chemin du fichier d'entrée à lire 
  - **codec** (Codec) : encodage du fichier d'entrée 
  - **sampleSep** (String) : séparateur de frames
  - **numberSep** (String): séparateur de nombres
- **output** (Path) : Chemin du fichier de sortie

## Sound

Module destiné à l'ingestion continue de son.
- Utilisation : 
    - `sbt sound/run` puis parler dans le micro
    - un fichier sera créé à l'emplacement défini par le paramètre **localConfiguration.outputFile** du fichier [reference.conf](sound/src/main/resources/reference.conf)
    - une ligne de ce fichier correspond à un enregistrement individuel


- Utilisation externe : 
    - Utiliser [WordSourceLive](sound/src/main/scala/com/jliermann/sound/process/WordSourceLive.scala) comme Source Akka
    - Fournir en paramètre une **soundConfiguration** valide ainsi qu'un [TargetDataLine](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/sound/sampled/TargetDataLine.html) comme entrée audio

#### Enregistrements

Un **Enregistrement** correspond à une séquence parlée ininterrompue (ou suffisamment peu interrompus).  
Il est composé de plusieurs **Frames**, fenêtres de temps fixes qui peuvent être soit **parlées** (signal sonore existant),
soit **silencieuses** (signal suffisamment faible pour être forcé à 0).  

Un exemple enregistrement dans le fichier de sortie sera :  
`{[1.234343;0.43343;...;-1.56]---SILENT---...---[0.275;0.2753573;...;-0.000425]---[-1.225725;0.444;...;0.147]}`

#### Paramètres

##### soundConfiguration
Configuration utilisable pour une utilisation externe du module.
- **audioFormat** : *Paramètres de [java AudioFormat](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/javax/sound/sampled/AudioFormat.html) gérant l'ingestion brute du son*
    - **sampleRate** (Int) : Fréquence d'échantillonnage de l'entrée en élements/seconde
    - **sampleSizeInBits** (Int) : Taille en Bits d'un élément échantillonné
    - **channels** (Int) : Nombre de canaux d'entrée (1=mono, 2=stéréo)
    - **signed** (bool): Encodage signé (ou non) des éléments échantillonnés
    - **bigEndian** (bool): Encodage big-endian (ou non) des éléments échantillonnés


- **samplingRate** : *Paramètres de régularisation du son brut échantillonné*
    - **samples** (Int) : Nombre d'éléments d'une Frame après aggrégation
    - **timeUnit** (FiniteDuration) : Longueur d'une Frame, en temps


- **overlapping** (Double, [0; 1[) : quantité de chaque Frame superposée à la précédente pour consolidation 

- **limitPitchedFrame** (Double) : Seuil arbitraire utilisé pour définir si une Frame est "parlée" ou "silencieuse". Diminuer pour plus de sensibilité au "parler"

- **silentSeparator** (Int) : Nombre de Frame silencieuses consécutives nécessaires pour terminer un enregistrement et commencer le suivant

##### localConfiguration
Configuration nécessaire à des fins de démo

- **outputFile** (Path) : Chemin du fichier de sortie ou seront sauvegardés les enregistrements 
