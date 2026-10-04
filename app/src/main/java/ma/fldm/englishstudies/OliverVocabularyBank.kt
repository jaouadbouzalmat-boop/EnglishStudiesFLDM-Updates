package ma.fldm.englishstudies

/**
 * Banque locale de vocabulaire pour Oliver Twist.
 *
 * La banque contient une fiche complète pour chaque mot :
 * - mot anglais
 * - prononciation
 * - définition anglaise simple
 * - traduction française
 * - traduction arabe
 * - exemple anglais + traductions
 * - nom prévu de l'illustration locale
 *
 * Les images seront ajoutées dans res/drawable avec le préfixe :
 * vocab_<mot>
 *
 * Le moteur peut extraire automatiquement les mots présents dans la page
 * actuellement affichée.
 */
object OliverVocabularyBank {

    private fun item(
        word: String,
        pronunciation: String,
        definitionEn: String,
        translationFr: String,
        translationAr: String,
        exampleEn: String,
        exampleFr: String,
        exampleAr: String
    ): OliverVocabularyItem {
        val safeName = word
            .lowercase()
            .replace(Regex("[^a-z0-9]+"), "_")
            .trim('_')

        return OliverVocabularyItem(
            word = word,
            pronunciation = pronunciation,
            audioText = word,
            definitionEn = definitionEn,
            translationFr = translationFr,
            translationAr = translationAr,
            exampleEn = exampleEn,
            exampleFr = exampleFr,
            exampleAr = exampleAr,
            illustrationName = "vocab_$safeName"
        )
    }

    val all: List<OliverVocabularyItem> = listOf(
        item(
            word = "workhouse",
            pronunciation = "/ˈwɜːrkhaʊs/",
            definitionEn = "a place where poor people were given food and shelter in return for work",
            translationFr = "maison de travail pour les pauvres",
            translationAr = "دار لإيواء وعمل الفقراء",
            exampleEn = "Oliver was born in a workhouse.",
            exampleFr = "Oliver est né dans une maison de travail.",
            exampleAr = "وُلد أوليفر في دار لإيواء الفقراء."
        ),
        item(
            word = "orphan",
            pronunciation = "/ˈɔːrfən/",
            definitionEn = "a child whose parents have died",
            translationFr = "orphelin / orpheline",
            translationAr = "يتيم",
            exampleEn = "Oliver was an orphan.",
            exampleFr = "Oliver était orphelin.",
            exampleAr = "كان أوليفر يتيماً."
        ),
        item(
            word = "parish",
            pronunciation = "/ˈpærɪʃ/",
            definitionEn = "a local church district and its community",
            translationFr = "paroisse",
            translationAr = "أبرشية / رعية",
            exampleEn = "The parish cared for poor children.",
            exampleFr = "La paroisse s'occupait des enfants pauvres.",
            exampleAr = "كانت الرعية تهتم بالأطفال الفقراء."
        ),
        item(
            word = "pauper",
            pronunciation = "/ˈpɔːpər/",
            definitionEn = "a very poor person",
            translationFr = "indigent / personne très pauvre",
            translationAr = "فقير معدم",
            exampleEn = "The pauper had no money.",
            exampleFr = "L'indigent n'avait pas d'argent.",
            exampleAr = "لم يكن لدى الفقير المعدم مال."
        ),
        item(
            word = "infant",
            pronunciation = "/ˈɪnfənt/",
            definitionEn = "a very young baby",
            translationFr = "nourrisson",
            translationAr = "رضيع",
            exampleEn = "The infant was very weak.",
            exampleFr = "Le nourrisson était très faible.",
            exampleAr = "كان الرضيع ضعيفاً جداً."
        ),
        item(
            word = "newborn",
            pronunciation = "/ˈnuːbɔːrn/",
            definitionEn = "a baby that has recently been born",
            translationFr = "nouveau-né",
            translationAr = "حديث الولادة",
            exampleEn = "The newborn was sleeping.",
            exampleFr = "Le nouveau-né dormait.",
            exampleAr = "كان المولود الجديد نائماً."
        ),
        item(
            word = "birth",
            pronunciation = "/bɜːrθ/",
            definitionEn = "the act of being born",
            translationFr = "naissance",
            translationAr = "ولادة",
            exampleEn = "His birth was recorded in the parish.",
            exampleFr = "Sa naissance fut enregistrée dans la paroisse.",
            exampleAr = "سُجلت ولادته في الرعية."
        ),
        item(
            word = "cradle",
            pronunciation = "/ˈkreɪdəl/",
            definitionEn = "a small bed for a baby",
            translationFr = "berceau",
            translationAr = "مهد",
            exampleEn = "The baby slept in a cradle.",
            exampleFr = "Le bébé dormait dans un berceau.",
            exampleAr = "نام الطفل في مهد."
        ),
        item(
            word = "poverty",
            pronunciation = "/ˈpɒvərti/",
            definitionEn = "the state of being very poor",
            translationFr = "pauvreté",
            translationAr = "فقر",
            exampleEn = "Poverty affected many families.",
            exampleFr = "La pauvreté touchait de nombreuses familles.",
            exampleAr = "كان الفقر يؤثر في عائلات كثيرة."
        ),
        item(
            word = "hunger",
            pronunciation = "/ˈhʌŋɡər/",
            definitionEn = "the feeling of needing food",
            translationFr = "faim",
            translationAr = "جوع",
            exampleEn = "Hunger made the children weak.",
            exampleFr = "La faim affaiblissait les enfants.",
            exampleAr = "جعل الجوع الأطفال ضعفاء."
        ),
        item(
            word = "starving",
            pronunciation = "/ˈstɑːrvɪŋ/",
            definitionEn = "extremely hungry",
            translationFr = "affamé",
            translationAr = "جائع جداً",
            exampleEn = "The boys were starving.",
            exampleFr = "Les garçons mouraient de faim.",
            exampleAr = "كان الأولاد يتضورون جوعاً."
        ),
        item(
            word = "miserable",
            pronunciation = "/ˈmɪzərəbəl/",
            definitionEn = "very unhappy or uncomfortable",
            translationFr = "misérable / malheureux",
            translationAr = "بائس / تعيس",
            exampleEn = "He lived in miserable conditions.",
            exampleFr = "Il vivait dans des conditions misérables.",
            exampleAr = "كان يعيش في ظروف بائسة."
        ),
        item(
            word = "destitute",
            pronunciation = "/ˈdestɪtjuːt/",
            definitionEn = "without money, food, or other basic needs",
            translationFr = "démuni / sans ressources",
            translationAr = "معدم / بلا موارد",
            exampleEn = "The destitute family needed help.",
            exampleFr = "La famille démunie avait besoin d'aide.",
            exampleAr = "كانت الأسرة المعدمة بحاجة إلى المساعدة."
        ),
        item(
            word = "neglected",
            pronunciation = "/nɪˈɡlektɪd/",
            definitionEn = "not given proper care or attention",
            translationFr = "négligé",
            translationAr = "مهمل",
            exampleEn = "The neglected child looked exhausted.",
            exampleFr = "L'enfant négligé semblait épuisé.",
            exampleAr = "بدا الطفل المهمل مرهقاً."
        ),
        item(
            word = "feeble",
            pronunciation = "/ˈfiːbəl/",
            definitionEn = "weak and without much strength",
            translationFr = "faible",
            translationAr = "ضعيف",
            exampleEn = "Oliver gave a feeble cry.",
            exampleFr = "Oliver poussa un faible cri.",
            exampleAr = "أطلق أوليفر صرخة ضعيفة."
        ),
        item(
            word = "gasp",
            pronunciation = "/ɡɑːsp/",
            definitionEn = "to take a sudden breath",
            translationFr = "haleter",
            translationAr = "يلهث / يلتقط أنفاسه",
            exampleEn = "He gasped in surprise.",
            exampleFr = "Il haleta de surprise.",
            exampleAr = "لهث من شدة المفاجأة."
        ),
        item(
            word = "labour",
            pronunciation = "/ˈleɪbər/",
            definitionEn = "hard physical or mental work",
            translationFr = "travail",
            translationAr = "عمل / كدّ",
            exampleEn = "The children were forced to labour.",
            exampleFr = "Les enfants étaient forcés de travailler.",
            exampleAr = "أُجبر الأطفال على العمل."
        ),
        item(
            word = "mortal",
            pronunciation = "/ˈmɔːrtəl/",
            definitionEn = "able to die",
            translationFr = "mortel",
            translationAr = "فانٍ",
            exampleEn = "Every mortal life is limited.",
            exampleFr = "Toute vie mortelle est limitée.",
            exampleAr = "كل حياة بشرية فانية."
        ),
        item(
            word = "mortality",
            pronunciation = "/mɔːrˈtæləti/",
            definitionEn = "the state or rate of death",
            translationFr = "mortalité",
            translationAr = "وفيات / فناء",
            exampleEn = "The report discussed infant mortality.",
            exampleFr = "Le rapport parlait de la mortalité infantile.",
            exampleAr = "تحدث التقرير عن وفيات الأطفال."
        ),
        item(
            word = "sorrow",
            pronunciation = "/ˈsɒrəʊ/",
            definitionEn = "deep sadness",
            translationFr = "chagrin / tristesse profonde",
            translationAr = "حزن عميق",
            exampleEn = "He felt great sorrow.",
            exampleFr = "Il ressentait une grande tristesse.",
            exampleAr = "شعر بحزن عميق."
        ),
        item(
            word = "grief",
            pronunciation = "/ɡriːf/",
            definitionEn = "deep sadness after a loss",
            translationFr = "chagrin / deuil",
            translationAr = "حزن شديد / فجيعة",
            exampleEn = "She was overcome with grief.",
            exampleFr = "Elle était submergée par le chagrin.",
            exampleAr = "غلبها الحزن الشديد."
        ),
        item(
            word = "bewildered",
            pronunciation = "/bɪˈwɪldərd/",
            definitionEn = "confused because you do not understand what is happening",
            translationFr = "déconcerté",
            translationAr = "حائر / مرتبك",
            exampleEn = "Oliver looked bewildered.",
            exampleFr = "Oliver avait l'air déconcerté.",
            exampleAr = "بدا أوليفر حائراً."
        ),
        item(
            word = "frightened",
            pronunciation = "/ˈfraɪtənd/",
            definitionEn = "afraid or scared",
            translationFr = "effrayé",
            translationAr = "خائف",
            exampleEn = "The boy was frightened.",
            exampleFr = "Le garçon était effrayé.",
            exampleAr = "كان الصبي خائفاً."
        ),
        item(
            word = "terrified",
            pronunciation = "/ˈterɪfaɪd/",
            definitionEn = "extremely afraid",
            translationFr = "terrifié",
            translationAr = "مرعوب",
            exampleEn = "He was terrified by the noise.",
            exampleFr = "Il était terrifié par le bruit.",
            exampleAr = "كان مرعوباً من الضجيج."
        ),
        item(
            word = "afraid",
            pronunciation = "/əˈfreɪd/",
            definitionEn = "feeling fear",
            translationFr = "effrayé",
            translationAr = "خائف",
            exampleEn = "Oliver was afraid.",
            exampleFr = "Oliver avait peur.",
            exampleAr = "كان أوليفر خائفاً."
        ),
        item(
            word = "courage",
            pronunciation = "/ˈkʌrɪdʒ/",
            definitionEn = "the ability to face danger or difficulty",
            translationFr = "courage",
            translationAr = "شجاعة",
            exampleEn = "Oliver showed courage.",
            exampleFr = "Oliver a fait preuve de courage.",
            exampleAr = "أظهر أوليفر شجاعة."
        ),
        item(
            word = "brave",
            pronunciation = "/breɪv/",
            definitionEn = "showing courage",
            translationFr = "courageux",
            translationAr = "شجاع",
            exampleEn = "He was a brave child.",
            exampleFr = "C'était un enfant courageux.",
            exampleAr = "كان طفلاً شجاعاً."
        ),
        item(
            word = "kindness",
            pronunciation = "/ˈkaɪndnəs/",
            definitionEn = "the quality of being kind and helpful",
            translationFr = "gentillesse",
            translationAr = "لطف / إحسان",
            exampleEn = "Her kindness changed his life.",
            exampleFr = "Sa gentillesse a changé sa vie.",
            exampleAr = "غيّر لطفها حياته."
        ),
        item(
            word = "compassion",
            pronunciation = "/kəmˈpæʃən/",
            definitionEn = "sympathy for another person's suffering",
            translationFr = "compassion",
            translationAr = "رحمة / تعاطف",
            exampleEn = "She showed compassion to Oliver.",
            exampleFr = "Elle a montré de la compassion envers Oliver.",
            exampleAr = "أظهرت التعاطف مع أوليفر."
        ),
        item(
            word = "shelter",
            pronunciation = "/ˈʃeltər/",
            definitionEn = "a place that provides protection",
            translationFr = "abri",
            translationAr = "مأوى",
            exampleEn = "They found shelter for the night.",
            exampleFr = "Ils trouvèrent un abri pour la nuit.",
            exampleAr = "وجدوا مأوى لليلة."
        ),
        item(
            word = "attendant",
            pronunciation = "/əˈtendənt/",
            definitionEn = "a person whose job is to assist or care for others",
            translationFr = "préposé / gardien",
            translationAr = "موظف / مرافق",
            exampleEn = "An attendant cared for the child.",
            exampleFr = "Un préposé s'occupait de l'enfant.",
            exampleAr = "اعتنى موظف بالطفل."
        ),
        item(
            word = "recovery",
            pronunciation = "/rɪˈkʌvəri/",
            definitionEn = "the process of becoming well again",
            translationFr = "rétablissement",
            translationAr = "تعافٍ",
            exampleEn = "His recovery was slow.",
            exampleFr = "Son rétablissement fut lent.",
            exampleAr = "كان تعافيه بطيئاً."
        ),
        item(
            word = "murmur",
            pronunciation = "/ˈmɜːrmər/",
            definitionEn = "to speak very quietly",
            translationFr = "murmurer",
            translationAr = "يتمتم",
            exampleEn = "He murmured an answer.",
            exampleFr = "Il murmura une réponse.",
            exampleAr = "تمتم بإجابة."
        ),
        item(
            word = "solemn",
            pronunciation = "/ˈsɒləm/",
            definitionEn = "serious and formal",
            translationFr = "solennel",
            translationAr = "جاد / مهيب",
            exampleEn = "The room became solemn.",
            exampleFr = "La pièce devint solennelle.",
            exampleAr = "أصبحت الغرفة مهيبة."
        ),
        item(
            word = "beg",
            pronunciation = "/beɡ/",
            definitionEn = "to ask strongly for something",
            translationFr = "supplier / mendier",
            translationAr = "يتوسل / يستجدي",
            exampleEn = "Oliver begged for food.",
            exampleFr = "Oliver demanda de la nourriture avec insistance.",
            exampleAr = "توسل أوليفر من أجل الطعام."
        ),
        item(
            word = "plead",
            pronunciation = "/pliːd/",
            definitionEn = "to ask or argue strongly for something",
            translationFr = "supplier / plaider",
            translationAr = "يتوسل / يلتمس",
            exampleEn = "He pleaded for mercy.",
            exampleFr = "Il implora la pitié.",
            exampleAr = "توسل من أجل الرحمة."
        ),
        item(
            word = "fled",
            pronunciation = "/fled/",
            definitionEn = "past tense of flee; ran away",
            translationFr = "s'enfuit",
            translationAr = "هرب",
            exampleEn = "Oliver fled from danger.",
            exampleFr = "Oliver s'enfuit du danger.",
            exampleAr = "هرب أوليفر من الخطر."
        ),
        item(
            word = "flee",
            pronunciation = "/fliː/",
            definitionEn = "to run away from danger",
            translationFr = "fuir",
            translationAr = "يهرب",
            exampleEn = "They fled the house.",
            exampleFr = "Ils fuirent la maison.",
            exampleAr = "هربوا من المنزل."
        ),
        item(
            word = "escape",
            pronunciation = "/ɪˈskeɪp/",
            definitionEn = "to get away from a dangerous or controlled place",
            translationFr = "s'échapper",
            translationAr = "يهرب / يفلت",
            exampleEn = "Oliver wanted to escape.",
            exampleFr = "Oliver voulait s'échapper.",
            exampleAr = "أراد أوليفر الهرب."
        ),
        item(
            word = "encounter",
            pronunciation = "/ɪnˈkaʊntər/",
            definitionEn = "to meet or experience something unexpectedly",
            translationFr = "rencontrer",
            translationAr = "يواجه / يصادف",
            exampleEn = "He encountered a stranger.",
            exampleFr = "Il rencontra un inconnu.",
            exampleAr = "صادف شخصاً غريباً."
        ),
        item(
            word = "robbery",
            pronunciation = "/ˈrɒbəri/",
            definitionEn = "the crime of stealing from a person or place",
            translationFr = "vol / cambriolage",
            translationAr = "سرقة / سطو",
            exampleEn = "The robbery caused panic.",
            exampleFr = "Le vol causa de la panique.",
            exampleAr = "سببت السرقة حالة من الذعر."
        ),
        item(
            word = "thief",
            pronunciation = "/θiːf/",
            definitionEn = "a person who steals",
            translationFr = "voleur",
            translationAr = "لص",
            exampleEn = "The thief ran away.",
            exampleFr = "Le voleur s'enfuit.",
            exampleAr = "هرب اللص."
        ),
        item(
            word = "thieves",
            pronunciation = "/θiːvz/",
            definitionEn = "plural of thief",
            translationFr = "voleurs",
            translationAr = "لصوص",
            exampleEn = "The thieves escaped.",
            exampleFr = "Les voleurs s'échappèrent.",
            exampleAr = "هرب اللصوص."
        ),
        item(
            word = "hideout",
            pronunciation = "/ˈhaɪdaʊt/",
            definitionEn = "a secret place where someone hides",
            translationFr = "cachette",
            translationAr = "مخبأ",
            exampleEn = "The gang returned to its hideout.",
            exampleFr = "Le gang retourna dans sa cachette.",
            exampleAr = "عاد أفراد العصابة إلى مخبئهم."
        ),
        item(
            word = "magistrate",
            pronunciation = "/ˈmædʒɪstreɪt/",
            definitionEn = "a local official who administers justice",
            translationFr = "magistrat",
            translationAr = "قاضٍ / مسؤول قضائي",
            exampleEn = "The magistrate heard the case.",
            exampleFr = "Le magistrat entendit l'affaire.",
            exampleAr = "نظر القاضي في القضية."
        ),
        item(
            word = "punishment",
            pronunciation = "/ˈpʌnɪʃmənt/",
            definitionEn = "something given to someone for doing wrong",
            translationFr = "punition",
            translationAr = "عقوبة",
            exampleEn = "The punishment was severe.",
            exampleFr = "La punition était sévère.",
            exampleAr = "كانت العقوبة قاسية."
        ),
        item(
            word = "whip",
            pronunciation = "/wɪp/",
            definitionEn = "a flexible object used for hitting",
            translationFr = "fouet",
            translationAr = "سوط",
            exampleEn = "The man carried a whip.",
            exampleFr = "L'homme portait un fouet.",
            exampleAr = "كان الرجل يحمل سوطاً."
        ),
        item(
            word = "work",
            pronunciation = "/wɜːrk/",
            definitionEn = "activity done to earn money or achieve something",
            translationFr = "travail",
            translationAr = "عمل",
            exampleEn = "They had to work hard.",
            exampleFr = "Ils devaient travailler dur.",
            exampleAr = "كان عليهم العمل بجد."
        ),
        item(
            word = "labourer",
            pronunciation = "/ˈleɪbərər/",
            definitionEn = "a person who does physical work",
            translationFr = "ouvrier",
            translationAr = "عامل يدوي",
            exampleEn = "The labourer worked all day.",
            exampleFr = "L'ouvrier travaillait toute la journée.",
            exampleAr = "عمل العامل طوال اليوم."
        ),
        item(
            word = "master",
            pronunciation = "/ˈmæstər/",
            definitionEn = "a man in authority or control",
            translationFr = "maître",
            translationAr = "سيد / مسؤول",
            exampleEn = "The master gave an order.",
            exampleFr = "Le maître donna un ordre.",
            exampleAr = "أعطى السيد أمراً."
        ),
        item(
            word = "mistress",
            pronunciation = "/ˈmɪstrəs/",
            definitionEn = "a woman in charge of a household or institution",
            translationFr = "maîtresse",
            translationAr = "سيدة / مسؤولة",
            exampleEn = "The mistress entered the room.",
            exampleFr = "La maîtresse entra dans la pièce.",
            exampleAr = "دخلت السيدة الغرفة."
        ),
        item(
            word = "beadle",
            pronunciation = "/ˈbiːdəl/",
            definitionEn = "a minor parish official with administrative duties",
            translationFr = "appariteur paroissial",
            translationAr = "موظف الرعية",
            exampleEn = "The beadle spoke to the children.",
            exampleFr = "L'appariteur paroissial parla aux enfants.",
            exampleAr = "تحدث موظف الرعية إلى الأطفال."
        ),
        item(
            word = "undertaker",
            pronunciation = "/ˈʌndərteɪkər/",
            definitionEn = "a person whose business is arranging funerals",
            translationFr = "entrepreneur de pompes funèbres",
            translationAr = "متعهد دفن / جنازات",
            exampleEn = "The undertaker arrived early.",
            exampleFr = "L'entrepreneur de pompes funèbres arriva tôt.",
            exampleAr = "وصل متعهد الدفن مبكراً."
        ),
        item(
            word = "surgeon",
            pronunciation = "/ˈsɜːrdʒən/",
            definitionEn = "a doctor who performs operations",
            translationFr = "chirurgien",
            translationAr = "جرّاح",
            exampleEn = "The surgeon examined Oliver.",
            exampleFr = "Le chirurgien examina Oliver.",
            exampleAr = "فحص الجرّاح أوليفر."
        ),
        item(
            word = "doctor",
            pronunciation = "/ˈdɒktər/",
            definitionEn = "a medical professional",
            translationFr = "médecin",
            translationAr = "طبيب",
            exampleEn = "The doctor came quickly.",
            exampleFr = "Le médecin arriva rapidement.",
            exampleAr = "جاء الطبيب بسرعة."
        ),
        item(
            word = "patient",
            pronunciation = "/ˈpeɪʃənt/",
            definitionEn = "a person receiving medical treatment",
            translationFr = "patient",
            translationAr = "مريض",
            exampleEn = "The patient needed rest.",
            exampleFr = "Le patient avait besoin de repos.",
            exampleAr = "كان المريض بحاجة إلى الراحة."
        ),
        item(
            word = "medicine",
            pronunciation = "/ˈmedɪsɪn/",
            definitionEn = "a substance used to treat illness",
            translationFr = "médicament",
            translationAr = "دواء",
            exampleEn = "The doctor gave him medicine.",
            exampleFr = "Le médecin lui donna un médicament.",
            exampleAr = "أعطاه الطبيب دواء."
        ),
        item(
            word = "illness",
            pronunciation = "/ˈɪlnəs/",
            definitionEn = "a disease or state of being unwell",
            translationFr = "maladie",
            translationAr = "مرض",
            exampleEn = "His illness lasted several days.",
            exampleFr = "Sa maladie dura plusieurs jours.",
            exampleAr = "استمر مرضه عدة أيام."
        ),
        item(
            word = "weakness",
            pronunciation = "/ˈwiːknəs/",
            definitionEn = "a lack of physical or mental strength",
            translationFr = "faiblesse",
            translationAr = "ضعف",
            exampleEn = "Hunger caused weakness.",
            exampleFr = "La faim causa de la faiblesse.",
            exampleAr = "سبب الجوع ضعفاً."
        ),
        item(
            word = "strength",
            pronunciation = "/streŋθ/",
            definitionEn = "the quality of being physically or mentally strong",
            translationFr = "force",
            translationAr = "قوة",
            exampleEn = "He recovered his strength.",
            exampleFr = "Il retrouva ses forces.",
            exampleAr = "استعاد قوته."
        ),
        item(
            word = "exhausted",
            pronunciation = "/ɪɡˈzɔːstɪd/",
            definitionEn = "extremely tired",
            translationFr = "épuisé",
            translationAr = "مرهق",
            exampleEn = "The children were exhausted.",
            exampleFr = "Les enfants étaient épuisés.",
            exampleAr = "كان الأطفال مرهقين."
        ),
        item(
            word = "tired",
            pronunciation = "/ˈtaɪərd/",
            definitionEn = "needing rest",
            translationFr = "fatigué",
            translationAr = "متعب",
            exampleEn = "Oliver was tired.",
            exampleFr = "Oliver était fatigué.",
            exampleAr = "كان أوليفر متعباً."
        ),
        item(
            word = "cold",
            pronunciation = "/koʊld/",
            definitionEn = "having a low temperature",
            translationFr = "froid",
            translationAr = "بارد",
            exampleEn = "It was a cold morning.",
            exampleFr = "C'était un matin froid.",
            exampleAr = "كان صباحاً بارداً."
        ),
        item(
            word = "warm",
            pronunciation = "/wɔːrm/",
            definitionEn = "having a comfortable high temperature",
            translationFr = "chaud",
            translationAr = "دافئ",
            exampleEn = "The room was warm.",
            exampleFr = "La pièce était chaude.",
            exampleAr = "كانت الغرفة دافئة."
        ),
        item(
            word = "dark",
            pronunciation = "/dɑːrk/",
            definitionEn = "with little or no light",
            translationFr = "sombre",
            translationAr = "مظلم",
            exampleEn = "The street was dark.",
            exampleFr = "La rue était sombre.",
            exampleAr = "كان الشارع مظلماً."
        ),
        item(
            word = "dim",
            pronunciation = "/dɪm/",
            definitionEn = "not brightly lit",
            translationFr = "faiblement éclairé",
            translationAr = "خافت الإضاءة",
            exampleEn = "A dim light filled the room.",
            exampleFr = "Une faible lumière éclairait la pièce.",
            exampleAr = "ملأ ضوء خافت الغرفة."
        ),
        item(
            word = "gloomy",
            pronunciation = "/ˈɡluːmi/",
            definitionEn = "dark, sad, and depressing",
            translationFr = "sombre et triste",
            translationAr = "قاتم / كئيب",
            exampleEn = "The house looked gloomy.",
            exampleFr = "La maison avait l'air sombre.",
            exampleAr = "بدا المنزل كئيباً."
        ),
        item(
            word = "quiet",
            pronunciation = "/ˈkwaɪət/",
            definitionEn = "making little or no noise",
            translationFr = "calme",
            translationAr = "هادئ",
            exampleEn = "The room was quiet.",
            exampleFr = "La pièce était calme.",
            exampleAr = "كانت الغرفة هادئة."
        ),
        item(
            word = "silent",
            pronunciation = "/ˈsaɪlənt/",
            definitionEn = "without sound",
            translationFr = "silencieux",
            translationAr = "صامت",
            exampleEn = "Everyone became silent.",
            exampleFr = "Tout le monde devint silencieux.",
            exampleAr = "أصبح الجميع صامتين."
        ),
        item(
            word = "noise",
            pronunciation = "/nɔɪz/",
            definitionEn = "a loud or unpleasant sound",
            translationFr = "bruit",
            translationAr = "ضجيج",
            exampleEn = "The noise woke him.",
            exampleFr = "Le bruit le réveilla.",
            exampleAr = "أيقظه الضجيج."
        ),
        item(
            word = "shout",
            pronunciation = "/ʃaʊt/",
            definitionEn = "to speak very loudly",
            translationFr = "crier",
            translationAr = "يصرخ",
            exampleEn = "The man shouted at Oliver.",
            exampleFr = "L'homme cria sur Oliver.",
            exampleAr = "صرخ الرجل في وجه أوليفر."
        ),
        item(
            word = "whisper",
            pronunciation = "/ˈwɪspər/",
            definitionEn = "to speak very quietly",
            translationFr = "chuchoter",
            translationAr = "يهمس",
            exampleEn = "She whispered his name.",
            exampleFr = "Elle chuchota son nom.",
            exampleAr = "همست باسمه."
        ),
        item(
            word = "cry",
            pronunciation = "/kraɪ/",
            definitionEn = "to produce tears or a loud expression of pain",
            translationFr = "pleurer / crier",
            translationAr = "يبكي / يصرخ",
            exampleEn = "The child began to cry.",
            exampleFr = "L'enfant commença à pleurer.",
            exampleAr = "بدأ الطفل بالبكاء."
        ),
        item(
            word = "sob",
            pronunciation = "/sɒb/",
            definitionEn = "to cry with short breaths",
            translationFr = "sangloter",
            translationAr = "ينتحب",
            exampleEn = "He sobbed quietly.",
            exampleFr = "Il sanglota doucement.",
            exampleAr = "انتحب بهدوء."
        ),
        item(
            word = "smile",
            pronunciation = "/smaɪl/",
            definitionEn = "to make a happy expression",
            translationFr = "sourire",
            translationAr = "يبتسم",
            exampleEn = "She gave him a smile.",
            exampleFr = "Elle lui adressa un sourire.",
            exampleAr = "ابتسمت له."
        ),
        item(
            word = "laugh",
            pronunciation = "/læf/",
            definitionEn = "to make a sound showing amusement",
            translationFr = "rire",
            translationAr = "يضحك",
            exampleEn = "They laughed together.",
            exampleFr = "Ils rirent ensemble.",
            exampleAr = "ضحكوا معاً."
        ),
        item(
            word = "anger",
            pronunciation = "/ˈæŋɡər/",
            definitionEn = "a strong feeling of annoyance or hostility",
            translationFr = "colère",
            translationAr = "غضب",
            exampleEn = "His anger grew.",
            exampleFr = "Sa colère augmenta.",
            exampleAr = "ازداد غضبه."
        ),
        item(
            word = "angry",
            pronunciation = "/ˈæŋɡri/",
            definitionEn = "feeling strong annoyance",
            translationFr = "en colère",
            translationAr = "غاضب",
            exampleEn = "The master was angry.",
            exampleFr = "Le maître était en colère.",
            exampleAr = "كان السيد غاضباً."
        ),
        item(
            word = "jealous",
            pronunciation = "/ˈdʒeləs/",
            definitionEn = "upset because someone has something you want",
            translationFr = "jaloux",
            translationAr = "غيور / حاقد",
            exampleEn = "He was jealous of the boy.",
            exampleFr = "Il était jaloux du garçon.",
            exampleAr = "كان يغار من الصبي."
        ),
        item(
            word = "suspicious",
            pronunciation = "/səˈspɪʃəs/",
            definitionEn = "feeling that something is wrong or dishonest",
            translationFr = "méfiant / suspect",
            translationAr = "مشكك / مرتاب",
            exampleEn = "She looked suspicious.",
            exampleFr = "Elle avait l'air méfiante.",
            exampleAr = "بدت مرتابة."
        ),
        item(
            word = "honest",
            pronunciation = "/ˈɒnɪst/",
            definitionEn = "truthful and not likely to cheat",
            translationFr = "honnête",
            translationAr = "صادق / نزيه",
            exampleEn = "Oliver tried to be honest.",
            exampleFr = "Oliver essayait d'être honnête.",
            exampleAr = "حاول أوليفر أن يكون صادقاً."
        ),
        item(
            word = "dishonest",
            pronunciation = "/dɪsˈɒnɪst/",
            definitionEn = "not truthful or fair",
            translationFr = "malhonnête",
            translationAr = "غير صادق / مخادع",
            exampleEn = "The dishonest man lied.",
            exampleFr = "L'homme malhonnête mentit.",
            exampleAr = "كذب الرجل المخادع."
        ),
        item(
            word = "innocent",
            pronunciation = "/ˈɪnəsənt/",
            definitionEn = "not guilty of a crime",
            translationFr = "innocent",
            translationAr = "بريء",
            exampleEn = "Oliver was innocent.",
            exampleFr = "Oliver était innocent.",
            exampleAr = "كان أوليفر بريئاً."
        ),
        item(
            word = "guilty",
            pronunciation = "/ˈɡɪlti/",
            definitionEn = "responsible for a crime or wrongdoing",
            translationFr = "coupable",
            translationAr = "مذنب",
            exampleEn = "The man was found guilty.",
            exampleFr = "L'homme fut reconnu coupable.",
            exampleAr = "أُدين الرجل."
        ),
        item(
            word = "criminal",
            pronunciation = "/ˈkrɪmɪnəl/",
            definitionEn = "a person who commits a crime",
            translationFr = "criminel",
            translationAr = "مجرم",
            exampleEn = "The criminal escaped.",
            exampleFr = "Le criminel s'échappa.",
            exampleAr = "هرب المجرم."
        ),
        item(
            word = "crime",
            pronunciation = "/kraɪm/",
            definitionEn = "an illegal act",
            translationFr = "crime",
            translationAr = "جريمة",
            exampleEn = "The crime was reported.",
            exampleFr = "Le crime fut signalé.",
            exampleAr = "تم الإبلاغ عن الجريمة."
        ),
        item(
            word = "gang",
            pronunciation = "/ɡæŋ/",
            definitionEn = "a group of people involved in organized wrongdoing",
            translationFr = "bande",
            translationAr = "عصابة",
            exampleEn = "The gang followed Fagin.",
            exampleFr = "La bande suivait Fagin.",
            exampleAr = "اتبعت العصابة فاجن."
        ),
        item(
            word = "pickpocket",
            pronunciation = "/ˈpɪkpɒkɪt/",
            definitionEn = "a person who steals from people's pockets",
            translationFr = "pickpocket",
            translationAr = "نشّال",
            exampleEn = "The pickpocket moved quickly.",
            exampleFr = "Le pickpocket bougeait rapidement.",
            exampleAr = "تحرك النشال بسرعة."
        ),
        item(
            word = "steal",
            pronunciation = "/stiːl/",
            definitionEn = "to take something without permission",
            translationFr = "voler",
            translationAr = "يسرق",
            exampleEn = "Do not steal money.",
            exampleFr = "Ne vole pas d'argent.",
            exampleAr = "لا تسرق المال."
        ),
        item(
            word = "stealing",
            pronunciation = "/ˈstiːlɪŋ/",
            definitionEn = "the act of taking something without permission",
            translationFr = "vol",
            translationAr = "سرقة",
            exampleEn = "Stealing is a crime.",
            exampleFr = "Voler est un crime.",
            exampleAr = "السرقة جريمة."
        ),
        item(
            word = "stolen",
            pronunciation = "/ˈstoʊlən/",
            definitionEn = "taken without permission",
            translationFr = "volé",
            translationAr = "مسروق",
            exampleEn = "The money was stolen.",
            exampleFr = "L'argent fut volé.",
            exampleAr = "سُرق المال."
        ),
        item(
            word = "capture",
            pronunciation = "/ˈkæptʃər/",
            definitionEn = "to catch and hold someone",
            translationFr = "capturer",
            translationAr = "يقبض على / يأسر",
            exampleEn = "The police captured him.",
            exampleFr = "La police le captura.",
            exampleAr = "ألقت الشرطة القبض عليه."
        ),
        item(
            word = "arrest",
            pronunciation = "/əˈrest/",
            definitionEn = "to take someone into legal custody",
            translationFr = "arrêter",
            translationAr = "يعتقل",
            exampleEn = "The officer arrested the thief.",
            exampleFr = "L'agent arrêta le voleur.",
            exampleAr = "اعتقل الشرطي اللص."
        ),
        item(
            word = "police",
            pronunciation = "/pəˈliːs/",
            definitionEn = "people whose job is to enforce the law",
            translationFr = "police",
            translationAr = "شرطة",
            exampleEn = "The police arrived.",
            exampleFr = "La police arriva.",
            exampleAr = "وصلت الشرطة."
        ),
        item(
            word = "officer",
            pronunciation = "/ˈɒfɪsər/",
            definitionEn = "a person with an official position, especially in the police",
            translationFr = "agent",
            translationAr = "ضابط / موظف",
            exampleEn = "The officer asked questions.",
            exampleFr = "L'agent posa des questions.",
            exampleAr = "طرح الضابط أسئلة."
        ),
        item(
            word = "law",
            pronunciation = "/lɔː/",
            definitionEn = "a rule made by a government",
            translationFr = "loi",
            translationAr = "قانون",
            exampleEn = "Everyone must obey the law.",
            exampleFr = "Tout le monde doit respecter la loi.",
            exampleAr = "يجب على الجميع احترام القانون."
        ),
        item(
            word = "court",
            pronunciation = "/kɔːrt/",
            definitionEn = "a place where legal cases are heard",
            translationFr = "tribunal",
            translationAr = "محكمة",
            exampleEn = "The case went to court.",
            exampleFr = "L'affaire passa devant le tribunal.",
            exampleAr = "وصلت القضية إلى المحكمة."
        ),
        item(
            word = "trial",
            pronunciation = "/ˈtraɪəl/",
            definitionEn = "a legal process for deciding a case",
            translationFr = "procès",
            translationAr = "محاكمة",
            exampleEn = "The trial began in the morning.",
            exampleFr = "Le procès commença le matin.",
            exampleAr = "بدأت المحاكمة صباحاً."
        ),
        item(
            word = "judge",
            pronunciation = "/dʒʌdʒ/",
            definitionEn = "a person who makes decisions in a court",
            translationFr = "juge",
            translationAr = "قاضٍ",
            exampleEn = "The judge listened carefully.",
            exampleFr = "Le juge écouta attentivement.",
            exampleAr = "استمع القاضي بعناية."
        ),
        item(
            word = "witness",
            pronunciation = "/ˈwɪtnəs/",
            definitionEn = "a person who sees an event or gives evidence in court",
            translationFr = "témoin",
            translationAr = "شاهد",
            exampleEn = "The witness told the truth.",
            exampleFr = "Le témoin dit la vérité.",
            exampleAr = "قال الشاهد الحقيقة."
        ),
        item(
            word = "evidence",
            pronunciation = "/ˈevɪdəns/",
            definitionEn = "facts or objects showing whether something is true",
            translationFr = "preuve",
            translationAr = "دليل",
            exampleEn = "The police found evidence.",
            exampleFr = "La police trouva des preuves.",
            exampleAr = "وجدت الشرطة دليلاً."
        ),
        item(
            word = "sentence",
            pronunciation = "/ˈsentəns/",
            definitionEn = "a punishment ordered by a court",
            translationFr = "peine",
            translationAr = "حكم / عقوبة",
            exampleEn = "The judge gave a sentence.",
            exampleFr = "Le juge prononça une peine.",
            exampleAr = "أصدر القاضي حكماً."
        ),
        item(
            word = "prison",
            pronunciation = "/ˈprɪzən/",
            definitionEn = "a place where criminals are kept as punishment",
            translationFr = "prison",
            translationAr = "سجن",
            exampleEn = "The criminal was sent to prison.",
            exampleFr = "Le criminel fut envoyé en prison.",
            exampleAr = "أُرسل المجرم إلى السجن."
        ),
        item(
            word = "cell",
            pronunciation = "/sel/",
            definitionEn = "a small room in a prison",
            translationFr = "cellule",
            translationAr = "زنزانة",
            exampleEn = "He stayed in a dark cell.",
            exampleFr = "Il resta dans une cellule sombre.",
            exampleAr = "بقي في زنزانة مظلمة."
        ),
        item(
            word = "guard",
            pronunciation = "/ɡɑːrd/",
            definitionEn = "a person who protects a place or person",
            translationFr = "gardien",
            translationAr = "حارس",
            exampleEn = "The guard watched the door.",
            exampleFr = "Le gardien surveillait la porte.",
            exampleAr = "راقب الحارس الباب."
        ),
        item(
            word = "key",
            pronunciation = "/kiː/",
            definitionEn = "a tool used to open or lock something",
            translationFr = "clé",
            translationAr = "مفتاح",
            exampleEn = "The guard held the key.",
            exampleFr = "Le gardien tenait la clé.",
            exampleAr = "كان الحارس يحمل المفتاح."
        ),
        item(
            word = "door",
            pronunciation = "/dɔːr/",
            definitionEn = "a movable barrier used to close an entrance",
            translationFr = "porte",
            translationAr = "باب",
            exampleEn = "He opened the door.",
            exampleFr = "Il ouvrit la porte.",
            exampleAr = "فتح الباب."
        ),
        item(
            word = "window",
            pronunciation = "/ˈwɪndoʊ/",
            definitionEn = "an opening with glass that lets in light",
            translationFr = "fenêtre",
            translationAr = "نافذة",
            exampleEn = "He looked through the window.",
            exampleFr = "Il regarda par la fenêtre.",
            exampleAr = "نظر من خلال النافذة."
        ),
        item(
            word = "street",
            pronunciation = "/striːt/",
            definitionEn = "a public road in a town or city",
            translationFr = "rue",
            translationAr = "شارع",
            exampleEn = "Oliver walked through the street.",
            exampleFr = "Oliver marcha dans la rue.",
            exampleAr = "مشى أوليفر في الشارع."
        ),
        item(
            word = "alley",
            pronunciation = "/ˈæli/",
            definitionEn = "a narrow passage between buildings",
            translationFr = "ruelle",
            translationAr = "زقاق",
            exampleEn = "The boys ran down the alley.",
            exampleFr = "Les garçons coururent dans la ruelle.",
            exampleAr = "ركض الأولاد في الزقاق."
        ),
        item(
            word = "lane",
            pronunciation = "/leɪn/",
            definitionEn = "a narrow road or path",
            translationFr = "voie / ruelle",
            translationAr = "ممر / طريق ضيق",
            exampleEn = "They walked along the lane.",
            exampleFr = "Ils marchèrent le long de la voie.",
            exampleAr = "ساروا على طول الطريق الضيق."
        ),
        item(
            word = "road",
            pronunciation = "/roʊd/",
            definitionEn = "a route for vehicles or people",
            translationFr = "route",
            translationAr = "طريق",
            exampleEn = "The road led to London.",
            exampleFr = "La route menait à Londres.",
            exampleAr = "كان الطريق يؤدي إلى لندن."
        ),
        item(
            word = "city",
            pronunciation = "/ˈsɪti/",
            definitionEn = "a large town",
            translationFr = "ville",
            translationAr = "مدينة",
            exampleEn = "London was a crowded city.",
            exampleFr = "Londres était une ville très peuplée.",
            exampleAr = "كانت لندن مدينة مكتظة."
        ),
        item(
            word = "village",
            pronunciation = "/ˈvɪlɪdʒ/",
            definitionEn = "a small community in the countryside",
            translationFr = "village",
            translationAr = "قرية",
            exampleEn = "He came from a small village.",
            exampleFr = "Il venait d'un petit village.",
            exampleAr = "جاء من قرية صغيرة."
        ),
        item(
            word = "house",
            pronunciation = "/haʊs/",
            definitionEn = "a building where people live",
            translationFr = "maison",
            translationAr = "منزل",
            exampleEn = "They entered the house.",
            exampleFr = "Ils entrèrent dans la maison.",
            exampleAr = "دخلوا المنزل."
        ),
        item(
            word = "room",
            pronunciation = "/ruːm/",
            definitionEn = "a part of a building enclosed by walls",
            translationFr = "pièce",
            translationAr = "غرفة",
            exampleEn = "The room was small.",
            exampleFr = "La pièce était petite.",
            exampleAr = "كانت الغرفة صغيرة."
        ),
        item(
            word = "kitchen",
            pronunciation = "/ˈkɪtʃɪn/",
            definitionEn = "a room where food is prepared",
            translationFr = "cuisine",
            translationAr = "مطبخ",
            exampleEn = "The cook worked in the kitchen.",
            exampleFr = "La cuisinière travaillait dans la cuisine.",
            exampleAr = "كانت الطاهية تعمل في المطبخ."
        ),
        item(
            word = "bedroom",
            pronunciation = "/ˈbedruːm/",
            definitionEn = "a room used for sleeping",
            translationFr = "chambre",
            translationAr = "غرفة نوم",
            exampleEn = "He went to the bedroom.",
            exampleFr = "Il alla dans la chambre.",
            exampleAr = "ذهب إلى غرفة النوم."
        ),
        item(
            word = "lodging",
            pronunciation = "/ˈlɒdʒɪŋ/",
            definitionEn = "a place where someone stays temporarily",
            translationFr = "logement",
            translationAr = "مسكن / إقامة",
            exampleEn = "They found cheap lodging.",
            exampleFr = "Ils trouvèrent un logement bon marché.",
            exampleAr = "وجدوا مسكناً رخيصاً."
        ),
        item(
            word = "stairs",
            pronunciation = "/sterz/",
            definitionEn = "steps leading between levels of a building",
            translationFr = "escaliers",
            translationAr = "سلالم",
            exampleEn = "He ran down the stairs.",
            exampleFr = "Il descendit les escaliers en courant.",
            exampleAr = "ركض نزولاً على السلالم."
        ),
        item(
            word = "floor",
            pronunciation = "/flɔːr/",
            definitionEn = "the surface people walk on inside a building",
            translationFr = "sol / étage",
            translationAr = "أرضية / طابق",
            exampleEn = "He sat on the floor.",
            exampleFr = "Il s'assit sur le sol.",
            exampleAr = "جلس على الأرض."
        ),
        item(
            word = "roof",
            pronunciation = "/ruːf/",
            definitionEn = "the covering on top of a building",
            translationFr = "toit",
            translationAr = "سطح",
            exampleEn = "Rain hit the roof.",
            exampleFr = "La pluie frappait le toit.",
            exampleAr = "ضرب المطر السطح."
        ),
        item(
            word = "chimney",
            pronunciation = "/ˈtʃɪmni/",
            definitionEn = "a structure carrying smoke from a fireplace",
            translationFr = "cheminée",
            translationAr = "مدخنة",
            exampleEn = "Smoke rose from the chimney.",
            exampleFr = "La fumée montait de la cheminée.",
            exampleAr = "تصاعد الدخان من المدخنة."
        ),
        item(
            word = "fireplace",
            pronunciation = "/ˈfaɪərpleɪs/",
            definitionEn = "the place in a room where a fire is made",
            translationFr = "cheminée",
            translationAr = "موقد / مدفأة",
            exampleEn = "They sat near the fireplace.",
            exampleFr = "Ils s'assirent près de la cheminée.",
            exampleAr = "جلسوا قرب المدفأة."
        ),
        item(
            word = "candle",
            pronunciation = "/ˈkændəl/",
            definitionEn = "a stick of wax used to give light",
            translationFr = "bougie",
            translationAr = "شمعة",
            exampleEn = "She lit a candle.",
            exampleFr = "Elle alluma une bougie.",
            exampleAr = "أشعلت شمعة."
        ),
        item(
            word = "lamp",
            pronunciation = "/læmp/",
            definitionEn = "a device that gives light",
            translationFr = "lampe",
            translationAr = "مصباح",
            exampleEn = "The lamp was burning.",
            exampleFr = "La lampe était allumée.",
            exampleAr = "كان المصباح مضاءً."
        ),
        item(
            word = "coat",
            pronunciation = "/koʊt/",
            definitionEn = "a piece of clothing worn over other clothes",
            translationFr = "manteau",
            translationAr = "معطف",
            exampleEn = "Oliver wore an old coat.",
            exampleFr = "Oliver portait un vieux manteau.",
            exampleAr = "كان أوليفر يرتدي معطفاً قديماً."
        ),
        item(
            word = "shirt",
            pronunciation = "/ʃɜːrt/",
            definitionEn = "a piece of clothing worn on the upper body",
            translationFr = "chemise",
            translationAr = "قميص",
            exampleEn = "His shirt was dirty.",
            exampleFr = "Sa chemise était sale.",
            exampleAr = "كان قميصه متسخاً."
        ),
        item(
            word = "shoes",
            pronunciation = "/ʃuːz/",
            definitionEn = "items worn on the feet",
            translationFr = "chaussures",
            translationAr = "أحذية",
            exampleEn = "His shoes were worn out.",
            exampleFr = "Ses chaussures étaient usées.",
            exampleAr = "كانت أحذيته بالية."
        ),
        item(
            word = "hat",
            pronunciation = "/hæt/",
            definitionEn = "a covering worn on the head",
            translationFr = "chapeau",
            translationAr = "قبعة",
            exampleEn = "He took off his hat.",
            exampleFr = "Il ôta son chapeau.",
            exampleAr = "خلع قبعته."
        ),
        item(
            word = "clothes",
            pronunciation = "/kloʊðz/",
            definitionEn = "things worn to cover the body",
            translationFr = "vêtements",
            translationAr = "ملابس",
            exampleEn = "His clothes were old.",
            exampleFr = "Ses vêtements étaient vieux.",
            exampleAr = "كانت ملابسه قديمة."
        ),
        item(
            word = "ragged",
            pronunciation = "/ˈræɡɪd/",
            definitionEn = "old, torn, or badly worn",
            translationFr = "déguenillé",
            translationAr = "رثّ / ممزق",
            exampleEn = "He wore ragged clothes.",
            exampleFr = "Il portait des vêtements déguenillés.",
            exampleAr = "كان يرتدي ملابس رثة."
        ),
        item(
            word = "dirty",
            pronunciation = "/ˈdɜːrti/",
            definitionEn = "not clean",
            translationFr = "sale",
            translationAr = "متسخ",
            exampleEn = "His hands were dirty.",
            exampleFr = "Ses mains étaient sales.",
            exampleAr = "كانت يداه متسختين."
        ),
        item(
            word = "clean",
            pronunciation = "/kliːn/",
            definitionEn = "free from dirt",
            translationFr = "propre",
            translationAr = "نظيف",
            exampleEn = "The room was clean.",
            exampleFr = "La pièce était propre.",
            exampleAr = "كانت الغرفة نظيفة."
        ),
        item(
            word = "food",
            pronunciation = "/fuːd/",
            definitionEn = "things that people or animals eat",
            translationFr = "nourriture",
            translationAr = "طعام",
            exampleEn = "The children needed food.",
            exampleFr = "Les enfants avaient besoin de nourriture.",
            exampleAr = "كان الأطفال بحاجة إلى الطعام."
        ),
        item(
            word = "bread",
            pronunciation = "/bred/",
            definitionEn = "food made from flour and baked",
            translationFr = "pain",
            translationAr = "خبز",
            exampleEn = "Oliver asked for bread.",
            exampleFr = "Oliver demanda du pain.",
            exampleAr = "طلب أوليفر الخبز."
        ),
        item(
            word = "soup",
            pronunciation = "/suːp/",
            definitionEn = "liquid food usually made by cooking vegetables or meat",
            translationFr = "soupe",
            translationAr = "حساء",
            exampleEn = "The soup was thin.",
            exampleFr = "La soupe était légère.",
            exampleAr = "كان الحساء خفيفاً."
        ),
        item(
            word = "meal",
            pronunciation = "/miːl/",
            definitionEn = "an occasion when food is eaten",
            translationFr = "repas",
            translationAr = "وجبة",
            exampleEn = "The meal was small.",
            exampleFr = "Le repas était petit.",
            exampleAr = "كانت الوجبة قليلة."
        ),
        item(
            word = "plate",
            pronunciation = "/pleɪt/",
            definitionEn = "a flat dish used for food",
            translationFr = "assiette",
            translationAr = "طبق",
            exampleEn = "He put the bread on a plate.",
            exampleFr = "Il posa le pain sur une assiette.",
            exampleAr = "وضع الخبز في طبق."
        ),
        item(
            word = "spoon",
            pronunciation = "/spuːn/",
            definitionEn = "a utensil for eating or serving food",
            translationFr = "cuillère",
            translationAr = "ملعقة",
            exampleEn = "He held a spoon.",
            exampleFr = "Il tenait une cuillère.",
            exampleAr = "كان يحمل ملعقة."
        ),
        item(
            word = "cup",
            pronunciation = "/kʌp/",
            definitionEn = "a small container for drinking",
            translationFr = "tasse",
            translationAr = "كوب / فنجان",
            exampleEn = "She gave him a cup of tea.",
            exampleFr = "Elle lui donna une tasse de thé.",
            exampleAr = "أعطته كوباً من الشاي."
        ),
        item(
            word = "water",
            pronunciation = "/ˈwɔːtər/",
            definitionEn = "the clear liquid people drink",
            translationFr = "eau",
            translationAr = "ماء",
            exampleEn = "He drank some water.",
            exampleFr = "Il but de l'eau.",
            exampleAr = "شرب بعض الماء."
        ),
        item(
            word = "tea",
            pronunciation = "/tiː/",
            definitionEn = "a hot drink made from leaves",
            translationFr = "thé",
            translationAr = "شاي",
            exampleEn = "They drank tea.",
            exampleFr = "Ils burent du thé.",
            exampleAr = "شربوا الشاي."
        ),
        item(
            word = "money",
            pronunciation = "/ˈmʌni/",
            definitionEn = "coins or notes used to buy things",
            translationFr = "argent",
            translationAr = "مال",
            exampleEn = "He had no money.",
            exampleFr = "Il n'avait pas d'argent.",
            exampleAr = "لم يكن لديه مال."
        ),
        item(
            word = "pocket",
            pronunciation = "/ˈpɒkɪt/",
            definitionEn = "a small part of clothing used to carry things",
            translationFr = "poche",
            translationAr = "جيب",
            exampleEn = "The coin was in his pocket.",
            exampleFr = "La pièce était dans sa poche.",
            exampleAr = "كانت القطعة النقدية في جيبه."
        ),
        item(
            word = "coin",
            pronunciation = "/kɔɪn/",
            definitionEn = "a small piece of metal used as money",
            translationFr = "pièce de monnaie",
            translationAr = "عملة معدنية",
            exampleEn = "He found a coin.",
            exampleFr = "Il trouva une pièce.",
            exampleAr = "وجد قطعة نقدية."
        ),
        item(
            word = "penny",
            pronunciation = "/ˈpeni/",
            definitionEn = "a small unit of British money",
            translationFr = "penny",
            translationAr = "بنس",
            exampleEn = "He saved every penny.",
            exampleFr = "Il économisait chaque penny.",
            exampleAr = "كان يدخر كل بنس."
        ),
        item(
            word = "shilling",
            pronunciation = "/ˈʃɪlɪŋ/",
            definitionEn = "an old British unit of money",
            translationFr = "shilling",
            translationAr = "شلن",
            exampleEn = "The item cost a shilling.",
            exampleFr = "L'objet coûtait un shilling.",
            exampleAr = "كان ثمن الشيء شلناً."
        ),
        item(
            word = "gentleman",
            pronunciation = "/ˈdʒentəlmən/",
            definitionEn = "a polite and well-mannered man",
            translationFr = "gentilhomme / monsieur",
            translationAr = "رجل محترم",
            exampleEn = "The gentleman helped Oliver.",
            exampleFr = "Le monsieur aida Oliver.",
            exampleAr = "ساعد الرجل المحترم أوليفر."
        ),
        item(
            word = "lady",
            pronunciation = "/ˈleɪdi/",
            definitionEn = "a polite word for a woman",
            translationFr = "dame",
            translationAr = "سيدة",
            exampleEn = "The lady spoke kindly.",
            exampleFr = "La dame parla gentiment.",
            exampleAr = "تحدثت السيدة بلطف."
        ),
        item(
            word = "gentle",
            pronunciation = "/ˈdʒentəl/",
            definitionEn = "kind, calm, and careful",
            translationFr = "doux / gentil",
            translationAr = "لطيف / رقيق",
            exampleEn = "He was gentle with the child.",
            exampleFr = "Il était doux avec l'enfant.",
            exampleAr = "كان لطيفاً مع الطفل."
        ),
        item(
            word = "rich",
            pronunciation = "/rɪtʃ/",
            definitionEn = "having a lot of money",
            translationFr = "riche",
            translationAr = "غني",
            exampleEn = "The rich man lived in a large house.",
            exampleFr = "L'homme riche vivait dans une grande maison.",
            exampleAr = "كان الرجل الغني يعيش في منزل كبير."
        ),
        item(
            word = "poor",
            pronunciation = "/pʊr/",
            definitionEn = "having little money or few possessions",
            translationFr = "pauvre",
            translationAr = "فقير",
            exampleEn = "The family was poor.",
            exampleFr = "La famille était pauvre.",
            exampleAr = "كانت الأسرة فقيرة."
        ),
        item(
            word = "wealth",
            pronunciation = "/welθ/",
            definitionEn = "a large amount of money or valuable possessions",
            translationFr = "richesse",
            translationAr = "ثروة",
            exampleEn = "His wealth was obvious.",
            exampleFr = "Sa richesse était évidente.",
            exampleAr = "كانت ثروته واضحة."
        ),
        item(
            word = "servant",
            pronunciation = "/ˈsɜːrvənt/",
            definitionEn = "a person employed to work in another person's home",
            translationFr = "serviteur",
            translationAr = "خادم",
            exampleEn = "The servant opened the door.",
            exampleFr = "Le serviteur ouvrit la porte.",
            exampleAr = "فتح الخادم الباب."
        ),
        item(
            word = "mastery",
            pronunciation = "/ˈmæstəri/",
            definitionEn = "great skill or control over something",
            translationFr = "maîtrise",
            translationAr = "إتقان / سيطرة",
            exampleEn = "He showed mastery of the task.",
            exampleFr = "Il montra une grande maîtrise de la tâche.",
            exampleAr = "أظهر إتقاناً كبيراً للمهمة."
        ),
        item(
            word = "apprentice",
            pronunciation = "/əˈprentɪs/",
            definitionEn = "a person learning a skilled job from an experienced worker",
            translationFr = "apprenti",
            translationAr = "متدرّب / تلميذ مهني",
            exampleEn = "He became an apprentice.",
            exampleFr = "Il devint apprenti.",
            exampleAr = "أصبح متدرّباً."
        ),
        item(
            word = "employer",
            pronunciation = "/ɪmˈplɔɪər/",
            definitionEn = "a person or organization that employs someone",
            translationFr = "employeur",
            translationAr = "مشغّل / صاحب عمل",
            exampleEn = "The employer offered work.",
            exampleFr = "L'employeur proposa du travail.",
            exampleAr = "عرض صاحب العمل عملاً."
        ),
        item(
            word = "employee",
            pronunciation = "/ɪmˈplɔɪiː/",
            definitionEn = "a person who is paid to work for someone",
            translationFr = "employé",
            translationAr = "موظف / عامل",
            exampleEn = "The employee arrived early.",
            exampleFr = "L'employé arriva tôt.",
            exampleAr = "وصل الموظف مبكراً."
        ),
        item(
            word = "career",
            pronunciation = "/kəˈrɪər/",
            definitionEn = "a person's working life or profession",
            translationFr = "carrière",
            translationAr = "مسيرة مهنية",
            exampleEn = "He wanted a better career.",
            exampleFr = "Il voulait une meilleure carrière.",
            exampleAr = "أراد مسيرة مهنية أفضل."
        ),
        item(
            word = "fortune",
            pronunciation = "/ˈfɔːrtʃuːn/",
            definitionEn = "a large amount of money or good luck",
            translationFr = "fortune",
            translationAr = "ثروة / حظ",
            exampleEn = "He inherited a fortune.",
            exampleFr = "Il hérita d'une fortune.",
            exampleAr = "ورث ثروة."
        ),
        item(
            word = "inherit",
            pronunciation = "/ɪnˈherɪt/",
            definitionEn = "to receive money or property from someone who has died",
            translationFr = "hériter",
            translationAr = "يرث",
            exampleEn = "He would inherit the house.",
            exampleFr = "Il hériterait de la maison.",
            exampleAr = "سيرث المنزل."
        ),
        item(
            word = "inheritance",
            pronunciation = "/ɪnˈherɪtəns/",
            definitionEn = "money or property received after someone's death",
            translationFr = "héritage",
            translationAr = "ميراث",
            exampleEn = "The inheritance changed his life.",
            exampleFr = "L'héritage changea sa vie.",
            exampleAr = "غيّر الميراث حياته."
        ),
        item(
            word = "family",
            pronunciation = "/ˈfæməli/",
            definitionEn = "a group of related people",
            translationFr = "famille",
            translationAr = "عائلة",
            exampleEn = "He wanted to find his family.",
            exampleFr = "Il voulait retrouver sa famille.",
            exampleAr = "أراد أن يجد عائلته."
        ),
        item(
            word = "parent",
            pronunciation = "/ˈperənt/",
            definitionEn = "a mother or father",
            translationFr = "parent",
            translationAr = "والد / أم أو أب",
            exampleEn = "He never knew his parents.",
            exampleFr = "Il ne connut jamais ses parents.",
            exampleAr = "لم يعرف والديه قط."
        ),
        item(
            word = "mother",
            pronunciation = "/ˈmʌðər/",
            definitionEn = "a female parent",
            translationFr = "mère",
            translationAr = "أم",
            exampleEn = "He remembered his mother.",
            exampleFr = "Il se souvenait de sa mère.",
            exampleAr = "تذكر أمه."
        ),
        item(
            word = "father",
            pronunciation = "/ˈfɑːðər/",
            definitionEn = "a male parent",
            translationFr = "père",
            translationAr = "أب",
            exampleEn = "He wanted to know his father.",
            exampleFr = "Il voulait connaître son père.",
            exampleAr = "أراد أن يعرف أباه."
        ),
        item(
            word = "brother",
            pronunciation = "/ˈbrʌðər/",
            definitionEn = "a male sibling",
            translationFr = "frère",
            translationAr = "أخ",
            exampleEn = "The brothers stood together.",
            exampleFr = "Les frères se tenaient ensemble.",
            exampleAr = "وقف الأخوان معاً."
        ),
        item(
            word = "sister",
            pronunciation = "/ˈsɪstər/",
            definitionEn = "a female sibling",
            translationFr = "sœur",
            translationAr = "أخت",
            exampleEn = "His sister helped him.",
            exampleFr = "Sa sœur l'aida.",
            exampleAr = "ساعدته أخته."
        ),
        item(
            word = "child",
            pronunciation = "/tʃaɪld/",
            definitionEn = "a young human being",
            translationFr = "enfant",
            translationAr = "طفل",
            exampleEn = "The child was hungry.",
            exampleFr = "L'enfant avait faim.",
            exampleAr = "كان الطفل جائعاً."
        ),
        item(
            word = "boy",
            pronunciation = "/bɔɪ/",
            definitionEn = "a male child",
            translationFr = "garçon",
            translationAr = "صبي",
            exampleEn = "The boy ran away.",
            exampleFr = "Le garçon s'enfuit.",
            exampleAr = "هرب الصبي."
        ),
        item(
            word = "girl",
            pronunciation = "/ɡɜːrl/",
            definitionEn = "a female child or young woman",
            translationFr = "fille",
            translationAr = "فتاة",
            exampleEn = "The girl helped Oliver.",
            exampleFr = "La fille aida Oliver.",
            exampleAr = "ساعدت الفتاة أوليفر."
        ),
        item(
            word = "youth",
            pronunciation = "/juːθ/",
            definitionEn = "the period of being young",
            translationFr = "jeunesse",
            translationAr = "شباب",
            exampleEn = "He spent his youth in London.",
            exampleFr = "Il passa sa jeunesse à Londres.",
            exampleAr = "قضى شبابه في لندن."
        ),
        item(
            word = "friend",
            pronunciation = "/frend/",
            definitionEn = "a person you know and like",
            translationFr = "ami",
            translationAr = "صديق",
            exampleEn = "He found a true friend.",
            exampleFr = "Il trouva un véritable ami.",
            exampleAr = "وجد صديقاً حقيقياً."
        ),
        item(
            word = "enemy",
            pronunciation = "/ˈenəmi/",
            definitionEn = "a person who opposes or harms you",
            translationFr = "ennemi",
            translationAr = "عدو",
            exampleEn = "The enemy watched him.",
            exampleFr = "L'ennemi le surveillait.",
            exampleAr = "راقبه العدو."
        ),
        item(
            word = "stranger",
            pronunciation = "/ˈstreɪndʒər/",
            definitionEn = "a person you do not know",
            translationFr = "inconnu",
            translationAr = "غريب",
            exampleEn = "A stranger approached him.",
            exampleFr = "Un inconnu s'approcha de lui.",
            exampleAr = "اقترب منه شخص غريب."
        ),
        item(
            word = "companion",
            pronunciation = "/kəmˈpænjən/",
            definitionEn = "a person who spends time with another",
            translationFr = "compagnon",
            translationAr = "رفيق",
            exampleEn = "He travelled with a companion.",
            exampleFr = "Il voyagea avec un compagnon.",
            exampleAr = "سافر مع رفيق."
        ),
        item(
            word = "neighbour",
            pronunciation = "/ˈneɪbər/",
            definitionEn = "a person who lives near you",
            translationFr = "voisin",
            translationAr = "جار",
            exampleEn = "The neighbour heard the noise.",
            exampleFr = "Le voisin entendit le bruit.",
            exampleAr = "سمع الجار الضجيج."
        ),
        item(
            word = "crowd",
            pronunciation = "/kraʊd/",
            definitionEn = "a large group of people together",
            translationFr = "foule",
            translationAr = "حشد",
            exampleEn = "A crowd gathered outside.",
            exampleFr = "Une foule se rassembla dehors.",
            exampleAr = "تجمع حشد في الخارج."
        ),
        item(
            word = "people",
            pronunciation = "/ˈpiːpəl/",
            definitionEn = "human beings in general or a group",
            translationFr = "gens / personnes",
            translationAr = "الناس",
            exampleEn = "Many people watched.",
            exampleFr = "Beaucoup de gens regardaient.",
            exampleAr = "راقب كثير من الناس."
        ),
        item(
            word = "meeting",
            pronunciation = "/ˈmiːtɪŋ/",
            definitionEn = "an occasion when people come together",
            translationFr = "réunion",
            translationAr = "اجتماع / لقاء",
            exampleEn = "They had a meeting.",
            exampleFr = "Ils eurent une réunion.",
            exampleAr = "عقدوا اجتماعاً."
        ),
        item(
            word = "conversation",
            pronunciation = "/ˌkɒnvərˈseɪʃən/",
            definitionEn = "a talk between two or more people",
            translationFr = "conversation",
            translationAr = "محادثة",
            exampleEn = "Their conversation was quiet.",
            exampleFr = "Leur conversation était calme.",
            exampleAr = "كانت محادثتهما هادئة."
        ),
        item(
            word = "question",
            pronunciation = "/ˈkwestʃən/",
            definitionEn = "something asked to get information",
            translationFr = "question",
            translationAr = "سؤال",
            exampleEn = "He asked a question.",
            exampleFr = "Il posa une question.",
            exampleAr = "طرح سؤالاً."
        ),
        item(
            word = "answer",
            pronunciation = "/ˈɑːnsər/",
            definitionEn = "something said in reply to a question",
            translationFr = "réponse",
            translationAr = "جواب",
            exampleEn = "Oliver gave an answer.",
            exampleFr = "Oliver donna une réponse.",
            exampleAr = "أعطى أوليفر جواباً."
        ),
        item(
            word = "order",
            pronunciation = "/ˈɔːrdər/",
            definitionEn = "a command or instruction",
            translationFr = "ordre",
            translationAr = "أمر",
            exampleEn = "The master gave an order.",
            exampleFr = "Le maître donna un ordre.",
            exampleAr = "أعطى السيد أمراً."
        ),
        item(
            word = "request",
            pronunciation = "/rɪˈkwest/",
            definitionEn = "a polite or formal demand for something",
            translationFr = "demande",
            translationAr = "طلب",
            exampleEn = "He made a request.",
            exampleFr = "Il fit une demande.",
            exampleAr = "قدم طلباً."
        ),
        item(
            word = "permission",
            pronunciation = "/pərˈmɪʃən/",
            definitionEn = "approval to do something",
            translationFr = "permission",
            translationAr = "إذن",
            exampleEn = "He asked for permission.",
            exampleFr = "Il demanda la permission.",
            exampleAr = "طلب الإذن."
        ),
        item(
            word = "refuse",
            pronunciation = "/rɪˈfjuːz/",
            definitionEn = "to say no to something",
            translationFr = "refuser",
            translationAr = "يرفض",
            exampleEn = "They refused his request.",
            exampleFr = "Ils refusèrent sa demande.",
            exampleAr = "رفضوا طلبه."
        ),
        item(
            word = "accept",
            pronunciation = "/əkˈsept/",
            definitionEn = "to agree to receive or do something",
            translationFr = "accepter",
            translationAr = "يقبل",
            exampleEn = "She accepted the offer.",
            exampleFr = "Elle accepta l'offre.",
            exampleAr = "قبلت العرض."
        ),
        item(
            word = "promise",
            pronunciation = "/ˈprɒmɪs/",
            definitionEn = "to say that you will certainly do something",
            translationFr = "promesse / promettre",
            translationAr = "وعد / يعد",
            exampleEn = "He promised to return.",
            exampleFr = "Il promit de revenir.",
            exampleAr = "وعد بالعودة."
        ),
        item(
            word = "trust",
            pronunciation = "/trʌst/",
            definitionEn = "to believe that someone is honest or reliable",
            translationFr = "faire confiance",
            translationAr = "يثق",
            exampleEn = "Oliver learned to trust him.",
            exampleFr = "Oliver apprit à lui faire confiance.",
            exampleAr = "تعلم أوليفر أن يثق به."
        ),
        item(
            word = "doubt",
            pronunciation = "/daʊt/",
            definitionEn = "a feeling of uncertainty",
            translationFr = "doute",
            translationAr = "شك",
            exampleEn = "She had doubts.",
            exampleFr = "Elle avait des doutes.",
            exampleAr = "كان لديها شكوك."
        ),
        item(
            word = "hope",
            pronunciation = "/hoʊp/",
            definitionEn = "a feeling that something good may happen",
            translationFr = "espoir",
            translationAr = "أمل",
            exampleEn = "Oliver never lost hope.",
            exampleFr = "Oliver ne perdit jamais espoir.",
            exampleAr = "لم يفقد أوليفر الأمل أبداً."
        ),
        item(
            word = "despair",
            pronunciation = "/dɪˈsper/",
            definitionEn = "a complete loss of hope",
            translationFr = "désespoir",
            translationAr = "يأس",
            exampleEn = "He fell into despair.",
            exampleFr = "Il tomba dans le désespoir.",
            exampleAr = "وقع في اليأس."
        ),
        item(
            word = "fear",
            pronunciation = "/fɪər/",
            definitionEn = "an unpleasant feeling caused by danger",
            translationFr = "peur",
            translationAr = "خوف",
            exampleEn = "Fear filled the room.",
            exampleFr = "La peur envahit la pièce.",
            exampleAr = "ملأ الخوف الغرفة."
        ),
        item(
            word = "danger",
            pronunciation = "/ˈdeɪndʒər/",
            definitionEn = "the possibility of harm",
            translationFr = "danger",
            translationAr = "خطر",
            exampleEn = "Oliver was in danger.",
            exampleFr = "Oliver était en danger.",
            exampleAr = "كان أوليفر في خطر."
        ),
        item(
            word = "safe",
            pronunciation = "/seɪf/",
            definitionEn = "protected from danger",
            translationFr = "en sécurité",
            translationAr = "آمن",
            exampleEn = "He was finally safe.",
            exampleFr = "Il était enfin en sécurité.",
            exampleAr = "أصبح أخيراً في أمان."
        ),
        item(
            word = "dangerous",
            pronunciation = "/ˈdeɪndʒərəs/",
            definitionEn = "likely to cause harm",
            translationFr = "dangereux",
            translationAr = "خطير",
            exampleEn = "The street was dangerous.",
            exampleFr = "La rue était dangereuse.",
            exampleAr = "كان الشارع خطيراً."
        ),
        item(
            word = "careful",
            pronunciation = "/ˈkerfəl/",
            definitionEn = "giving attention to avoid mistakes or danger",
            translationFr = "prudent",
            translationAr = "حذر",
            exampleEn = "Be careful with the door.",
            exampleFr = "Sois prudent avec la porte.",
            exampleAr = "كن حذراً مع الباب."
        ),
        item(
            word = "careless",
            pronunciation = "/ˈkerləs/",
            definitionEn = "not giving enough attention",
            translationFr = "négligent",
            translationAr = "مهمل",
            exampleEn = "His careless action caused trouble.",
            exampleFr = "Son geste négligent causa des problèmes.",
            exampleAr = "سبب تصرفه المهمل مشاكل."
        ),
        item(
            word = "secret",
            pronunciation = "/ˈsiːkrət/",
            definitionEn = "something hidden from others",
            translationFr = "secret",
            translationAr = "سر",
            exampleEn = "He kept the secret.",
            exampleFr = "Il garda le secret.",
            exampleAr = "احتفظ بالسر."
        ),
        item(
            word = "mystery",
            pronunciation = "/ˈmɪstəri/",
            definitionEn = "something difficult to explain or understand",
            translationFr = "mystère",
            translationAr = "لغز / سر غامض",
            exampleEn = "His past was a mystery.",
            exampleFr = "Son passé était un mystère.",
            exampleAr = "كان ماضيه لغزاً."
        ),
        item(
            word = "truth",
            pronunciation = "/truːθ/",
            definitionEn = "the real facts about something",
            translationFr = "vérité",
            translationAr = "حقيقة",
            exampleEn = "The truth was finally revealed.",
            exampleFr = "La vérité fut enfin révélée.",
            exampleAr = "انكشفت الحقيقة أخيراً."
        ),
        item(
            word = "lie",
            pronunciation = "/laɪ/",
            definitionEn = "a statement that is not true",
            translationFr = "mensonge",
            translationAr = "كذبة",
            exampleEn = "He told a lie.",
            exampleFr = "Il dit un mensonge.",
            exampleAr = "قال كذبة."
        ),
        item(
            word = "identity",
            pronunciation = "/aɪˈdentəti/",
            definitionEn = "who a person is",
            translationFr = "identité",
            translationAr = "هوية",
            exampleEn = "Oliver's identity was hidden.",
            exampleFr = "L'identité d'Oliver était cachée.",
            exampleAr = "كانت هوية أوليفر مخفية."
        ),
        item(
            word = "name",
            pronunciation = "/neɪm/",
            definitionEn = "a word used to identify a person or thing",
            translationFr = "nom",
            translationAr = "اسم",
            exampleEn = "He asked for her name.",
            exampleFr = "Il demanda son nom.",
            exampleAr = "سأل عن اسمها."
        ),
        item(
            word = "memory",
            pronunciation = "/ˈmeməri/",
            definitionEn = "something remembered",
            translationFr = "souvenir / mémoire",
            translationAr = "ذاكرة / ذكرى",
            exampleEn = "The event remained in his memory.",
            exampleFr = "L'événement resta dans sa mémoire.",
            exampleAr = "بقي الحدث في ذاكرته."
        ),
        item(
            word = "remember",
            pronunciation = "/rɪˈmembər/",
            definitionEn = "to bring something back into your mind",
            translationFr = "se souvenir",
            translationAr = "يتذكر",
            exampleEn = "He remembered his mother.",
            exampleFr = "Il se souvenait de sa mère.",
            exampleAr = "تذكر أمه."
        ),
        item(
            word = "forget",
            pronunciation = "/fərˈɡet/",
            definitionEn = "to fail to remember",
            translationFr = "oublier",
            translationAr = "ينسى",
            exampleEn = "He could not forget the event.",
            exampleFr = "Il ne pouvait pas oublier l'événement.",
            exampleAr = "لم يستطع نسيان الحدث."
        ),
        item(
            word = "reveal",
            pronunciation = "/rɪˈviːl/",
            definitionEn = "to make something known",
            translationFr = "révéler",
            translationAr = "يكشف",
            exampleEn = "The letter revealed the truth.",
            exampleFr = "La lettre révéla la vérité.",
            exampleAr = "كشفت الرسالة الحقيقة."
        ),
        item(
            word = "discover",
            pronunciation = "/dɪˈskʌvər/",
            definitionEn = "to find something for the first time",
            translationFr = "découvrir",
            translationAr = "يكتشف",
            exampleEn = "He discovered a secret.",
            exampleFr = "Il découvrit un secret.",
            exampleAr = "اكتشف سراً."
        ),
        item(
            word = "search",
            pronunciation = "/sɜːrtʃ/",
            definitionEn = "to look carefully for something",
            translationFr = "chercher",
            translationAr = "يبحث",
            exampleEn = "They searched the room.",
            exampleFr = "Ils fouillèrent la pièce.",
            exampleAr = "فتشوا الغرفة."
        ),
        item(
            word = "find",
            pronunciation = "/faɪnd/",
            definitionEn = "to discover or locate something",
            translationFr = "trouver",
            translationAr = "يجد",
            exampleEn = "He found the letter.",
            exampleFr = "Il trouva la lettre.",
            exampleAr = "وجد الرسالة."
        ),
        item(
            word = "lose",
            pronunciation = "/luːz/",
            definitionEn = "to no longer have something",
            translationFr = "perdre",
            translationAr = "يفقد",
            exampleEn = "He lost his way.",
            exampleFr = "Il se perdit.",
            exampleAr = "فقد طريقه."
        ),
        item(
            word = "follow",
            pronunciation = "/ˈfɒləʊ/",
            definitionEn = "to go after someone or something",
            translationFr = "suivre",
            translationAr = "يتبع",
            exampleEn = "The boys followed him.",
            exampleFr = "Les garçons le suivirent.",
            exampleAr = "تبع الأولاد الرجل."
        ),
        item(
            word = "lead",
            pronunciation = "/liːd/",
            definitionEn = "to guide or go in front of others",
            translationFr = "mener",
            translationAr = "يقود",
            exampleEn = "Fagin led the gang.",
            exampleFr = "Fagin dirigeait la bande.",
            exampleAr = "كان فاجن يقود العصابة."
        ),
        item(
            word = "chase",
            pronunciation = "/tʃeɪs/",
            definitionEn = "to run after someone to catch them",
            translationFr = "poursuivre",
            translationAr = "يطارد",
            exampleEn = "The police chased the thief.",
            exampleFr = "La police poursuivit le voleur.",
            exampleAr = "طاردت الشرطة اللص."
        ),
        item(
            word = "run",
            pronunciation = "/rʌn/",
            definitionEn = "to move quickly on foot",
            translationFr = "courir",
            translationAr = "يركض",
            exampleEn = "Oliver ran away.",
            exampleFr = "Oliver courut au loin.",
            exampleAr = "ركض أوليفر بعيداً."
        ),
        item(
            word = "walk",
            pronunciation = "/wɔːk/",
            definitionEn = "to move on foot",
            translationFr = "marcher",
            translationAr = "يمشي",
            exampleEn = "They walked through London.",
            exampleFr = "Ils marchèrent dans Londres.",
            exampleAr = "مشوا عبر لندن."
        ),
        item(
            word = "climb",
            pronunciation = "/klaɪm/",
            definitionEn = "to move up using hands and feet",
            translationFr = "grimper",
            translationAr = "يتسلق",
            exampleEn = "He climbed the stairs.",
            exampleFr = "Il monta les escaliers.",
            exampleAr = "صعد السلالم."
        ),
        item(
            word = "enter",
            pronunciation = "/ˈentər/",
            definitionEn = "to go into a place",
            translationFr = "entrer",
            translationAr = "يدخل",
            exampleEn = "They entered the house.",
            exampleFr = "Ils entrèrent dans la maison.",
            exampleAr = "دخلوا المنزل."
        ),
        item(
            word = "leave",
            pronunciation = "/liːv/",
            definitionEn = "to go away from a place",
            translationFr = "quitter",
            translationAr = "يغادر",
            exampleEn = "Oliver left the room.",
            exampleFr = "Oliver quitta la pièce.",
            exampleAr = "غادر أوليفر الغرفة."
        ),
        item(
            word = "return",
            pronunciation = "/rɪˈtɜːrn/",
            definitionEn = "to come or go back",
            translationFr = "retourner",
            translationAr = "يعود",
            exampleEn = "He returned home.",
            exampleFr = "Il retourna chez lui.",
            exampleAr = "عاد إلى المنزل."
        ),
        item(
            word = "arrive",
            pronunciation = "/əˈraɪv/",
            definitionEn = "to reach a place",
            translationFr = "arriver",
            translationAr = "يصل",
            exampleEn = "They arrived late.",
            exampleFr = "Ils arrivèrent tard.",
            exampleAr = "وصلوا متأخرين."
        ),
        item(
            word = "depart",
            pronunciation = "/dɪˈpɑːrt/",
            definitionEn = "to leave a place",
            translationFr = "partir",
            translationAr = "يغادر",
            exampleEn = "The coach departed at dawn.",
            exampleFr = "La voiture partit à l'aube.",
            exampleAr = "غادرت العربة عند الفجر."
        ),
        item(
            word = "travel",
            pronunciation = "/ˈtrævəl/",
            definitionEn = "to go from one place to another",
            translationFr = "voyager",
            translationAr = "يسافر",
            exampleEn = "They travelled to London.",
            exampleFr = "Ils voyagèrent jusqu'à Londres.",
            exampleAr = "سافروا إلى لندن."
        ),
        item(
            word = "journey",
            pronunciation = "/ˈdʒɜːrni/",
            definitionEn = "an act of travelling from one place to another",
            translationFr = "voyage",
            translationAr = "رحلة",
            exampleEn = "The journey was long.",
            exampleFr = "Le voyage fut long.",
            exampleAr = "كانت الرحلة طويلة."
        ),
        item(
            word = "coach",
            pronunciation = "/koʊtʃ/",
            definitionEn = "a large horse-drawn vehicle for passengers",
            translationFr = "diligence",
            translationAr = "عربة ركاب تجرها الخيول",
            exampleEn = "The coach left early.",
            exampleFr = "La diligence partit tôt.",
            exampleAr = "غادرت عربة الركاب مبكراً."
        ),
        item(
            word = "carriage",
            pronunciation = "/ˈkærɪdʒ/",
            definitionEn = "a vehicle pulled by horses",
            translationFr = "voiture / calèche",
            translationAr = "عربة تجرها الخيول",
            exampleEn = "The carriage stopped outside.",
            exampleFr = "La voiture s'arrêta dehors.",
            exampleAr = "توقفت العربة في الخارج."
        ),
        item(
            word = "horse",
            pronunciation = "/hɔːrs/",
            definitionEn = "a large animal used for riding or pulling vehicles",
            translationFr = "cheval",
            translationAr = "حصان",
            exampleEn = "The horse pulled the carriage.",
            exampleFr = "Le cheval tira la voiture.",
            exampleAr = "جر الحصان العربة."
        ),
        item(
            word = "horseman",
            pronunciation = "/ˈhɔːrsmən/",
            definitionEn = "a man who rides or works with horses",
            translationFr = "cavalier",
            translationAr = "فارس / راكب خيل",
            exampleEn = "The horseman rode quickly.",
            exampleFr = "Le cavalier avançait rapidement.",
            exampleAr = "ركب الفارس بسرعة."
        ),
        item(
            word = "mastermind",
            pronunciation = "/ˈmæstərmaɪnd/",
            definitionEn = "a person who plans a complicated activity",
            translationFr = "cerveau",
            translationAr = "العقل المدبر",
            exampleEn = "Fagin was the mastermind.",
            exampleFr = "Fagin était le cerveau.",
            exampleAr = "كان فاجن العقل المدبر."
        ),
        item(
            word = "scheme",
            pronunciation = "/skiːm/",
            definitionEn = "a plan, often secret or dishonest",
            translationFr = "complot / plan",
            translationAr = "خطة / مؤامرة",
            exampleEn = "They made a secret scheme.",
            exampleFr = "Ils élaborèrent un plan secret.",
            exampleAr = "وضعوا خطة سرية."
        ),
        item(
            word = "plot",
            pronunciation = "/plɒt/",
            definitionEn = "a secret plan to do something wrong",
            translationFr = "complot",
            translationAr = "مؤامرة",
            exampleEn = "The plot was dangerous.",
            exampleFr = "Le complot était dangereux.",
            exampleAr = "كانت المؤامرة خطيرة."
        ),
        item(
            word = "plan",
            pronunciation = "/plæn/",
            definitionEn = "a detailed intention for doing something",
            translationFr = "plan",
            translationAr = "خطة",
            exampleEn = "They made a plan.",
            exampleFr = "Ils firent un plan.",
            exampleAr = "وضعوا خطة."
        ),
        item(
            word = "trick",
            pronunciation = "/trɪk/",
            definitionEn = "an action intended to deceive someone",
            translationFr = "ruse",
            translationAr = "حيلة",
            exampleEn = "It was a clever trick.",
            exampleFr = "C'était une ruse habile.",
            exampleAr = "كانت حيلة ذكية."
        ),
        item(
            word = "cheat",
            pronunciation = "/tʃiːt/",
            definitionEn = "to act dishonestly to gain an advantage",
            translationFr = "tricher",
            translationAr = "يغش / يخدع",
            exampleEn = "He tried to cheat.",
            exampleFr = "Il essaya de tricher.",
            exampleAr = "حاول الغش."
        ),
        item(
            word = "deceive",
            pronunciation = "/dɪˈsiːv/",
            definitionEn = "to make someone believe something untrue",
            translationFr = "tromper",
            translationAr = "يخدع",
            exampleEn = "The man tried to deceive him.",
            exampleFr = "L'homme essaya de le tromper.",
            exampleAr = "حاول الرجل خداعه."
        ),
        item(
            word = "betray",
            pronunciation = "/bɪˈtreɪ/",
            definitionEn = "to be disloyal to someone",
            translationFr = "trahir",
            translationAr = "يخون",
            exampleEn = "He was afraid they would betray him.",
            exampleFr = "Il craignait qu'ils ne le trahissent.",
            exampleAr = "خاف من أن يخونوه."
        ),
        item(
            word = "loyal",
            pronunciation = "/ˈlɔɪəl/",
            definitionEn = "showing constant support",
            translationFr = "loyal",
            translationAr = "مخلص",
            exampleEn = "Nancy was loyal to Oliver.",
            exampleFr = "Nancy était loyale envers Oliver.",
            exampleAr = "كانت نانسي مخلصة لأوليفر."
        ),
        item(
            word = "faithful",
            pronunciation = "/ˈfeɪθfəl/",
            definitionEn = "loyal and trustworthy",
            translationFr = "fidèle",
            translationAr = "وفيّ",
            exampleEn = "He remained faithful to his friend.",
            exampleFr = "Il resta fidèle à son ami.",
            exampleAr = "بقي وفياً لصديقه."
        ),
        item(
            word = "mercy",
            pronunciation = "/ˈmɜːrsi/",
            definitionEn = "kindness shown to someone who deserves punishment",
            translationFr = "miséricorde",
            translationAr = "رحمة",
            exampleEn = "He begged for mercy.",
            exampleFr = "Il implora la miséricorde.",
            exampleAr = "توسل الرحمة."
        ),
        item(
            word = "forgive",
            pronunciation = "/fərˈɡɪv/",
            definitionEn = "to stop feeling angry about a wrong",
            translationFr = "pardonner",
            translationAr = "يسامح",
            exampleEn = "She chose to forgive him.",
            exampleFr = "Elle choisit de lui pardonner.",
            exampleAr = "اختارت أن تسامحه."
        ),
        item(
            word = "forgiveness",
            pronunciation = "/fərˈɡɪvnəs/",
            definitionEn = "the act of forgiving",
            translationFr = "pardon",
            translationAr = "مغفرة / عفو",
            exampleEn = "He asked for forgiveness.",
            exampleFr = "Il demanda pardon.",
            exampleAr = "طلب المغفرة."
        ),
        item(
            word = "pity",
            pronunciation = "/ˈpɪti/",
            definitionEn = "sadness caused by another person's suffering",
            translationFr = "pitié",
            translationAr = "شفقة",
            exampleEn = "She felt pity for him.",
            exampleFr = "Elle eut pitié de lui.",
            exampleAr = "شعرت بالشفقة عليه."
        ),
        item(
            word = "sympathy",
            pronunciation = "/ˈsɪmpəθi/",
            definitionEn = "understanding and concern for another person's suffering",
            translationFr = "sympathie",
            translationAr = "تعاطف",
            exampleEn = "He received sympathy.",
            exampleFr = "Il reçut de la sympathie.",
            exampleAr = "حظي بالتعاطف."
        ),
        item(
            word = "charity",
            pronunciation = "/ˈtʃærəti/",
            definitionEn = "help given to people who are poor or in need",
            translationFr = "charité",
            translationAr = "صدقة / عمل خيري",
            exampleEn = "The charity helped poor families.",
            exampleFr = "L'association caritative aidait les familles pauvres.",
            exampleAr = "ساعدت الجمعية الخيرية الأسر الفقيرة."
        ),
        item(
            word = "generous",
            pronunciation = "/ˈdʒenərəs/",
            definitionEn = "willing to give more than expected",
            translationFr = "généreux",
            translationAr = "كريم",
            exampleEn = "The gentleman was generous.",
            exampleFr = "Le gentleman était généreux.",
            exampleAr = "كان الرجل المحترم كريماً."
        ),
        item(
            word = "generosity",
            pronunciation = "/ˌdʒenəˈrɒsəti/",
            definitionEn = "the quality of giving freely",
            translationFr = "générosité",
            translationAr = "كرم",
            exampleEn = "His generosity helped many people.",
            exampleFr = "Sa générosité aida beaucoup de gens.",
            exampleAr = "ساعد كرمه كثيراً من الناس."
        ),
        item(
            word = "cruel",
            pronunciation = "/ˈkruːəl/",
            definitionEn = "causing pain deliberately or without care",
            translationFr = "cruel",
            translationAr = "قاسٍ",
            exampleEn = "The treatment was cruel.",
            exampleFr = "Le traitement était cruel.",
            exampleAr = "كانت المعاملة قاسية."
        ),
        item(
            word = "cruelty",
            pronunciation = "/ˈkruːəlti/",
            definitionEn = "deliberate or careless causing of pain",
            translationFr = "cruauté",
            translationAr = "قسوة",
            exampleEn = "The novel shows cruelty.",
            exampleFr = "Le roman montre la cruauté.",
            exampleAr = "تُظهر الرواية القسوة."
        ),
        item(
            word = "harsh",
            pronunciation = "/hɑːrʃ/",
            definitionEn = "severe, unpleasant, or difficult to endure",
            translationFr = "dur / sévère",
            translationAr = "قاسٍ / شديد",
            exampleEn = "The conditions were harsh.",
            exampleFr = "Les conditions étaient difficiles.",
            exampleAr = "كانت الظروف قاسية."
        ),
        item(
            word = "severe",
            pronunciation = "/sɪˈvɪər/",
            definitionEn = "very serious or strict",
            translationFr = "sévère",
            translationAr = "شديد / صارم",
            exampleEn = "The punishment was severe.",
            exampleFr = "La punition était sévère.",
            exampleAr = "كانت العقوبة شديدة."
        ),
        item(
            word = "strict",
            pronunciation = "/strɪkt/",
            definitionEn = "demanding that rules be followed",
            translationFr = "strict",
            translationAr = "صارم",
            exampleEn = "The master was strict.",
            exampleFr = "Le maître était strict.",
            exampleAr = "كان السيد صارماً."
        ),
        item(
            word = "rude",
            pronunciation = "/ruːd/",
            definitionEn = "not polite",
            translationFr = "impoli",
            translationAr = "فظ / غير مؤدب",
            exampleEn = "The boy was rude.",
            exampleFr = "Le garçon était impoli.",
            exampleAr = "كان الصبي فظاً."
        ),
        item(
            word = "polite",
            pronunciation = "/pəˈlaɪt/",
            definitionEn = "having good manners",
            translationFr = "poli",
            translationAr = "مهذب",
            exampleEn = "Oliver remained polite.",
            exampleFr = "Oliver resta poli.",
            exampleAr = "بقي أوليفر مهذباً."
        ),
        item(
            word = "humble",
            pronunciation = "/ˈhʌmbəl/",
            definitionEn = "not proud or arrogant",
            translationFr = "humble",
            translationAr = "متواضع",
            exampleEn = "He was humble despite his success.",
            exampleFr = "Il était humble malgré son succès.",
            exampleAr = "كان متواضعاً رغم نجاحه."
        ),
        item(
            word = "proud",
            pronunciation = "/praʊd/",
            definitionEn = "feeling pleasure because of your achievements",
            translationFr = "fier",
            translationAr = "فخور",
            exampleEn = "She was proud of him.",
            exampleFr = "Elle était fière de lui.",
            exampleAr = "كانت فخورة به."
        ),
        item(
            word = "ashamed",
            pronunciation = "/əˈʃeɪmd/",
            definitionEn = "feeling guilty or embarrassed",
            translationFr = "honteux",
            translationAr = "خجلان / يشعر بالعار",
            exampleEn = "He felt ashamed.",
            exampleFr = "Il avait honte.",
            exampleAr = "شعر بالخجل."
        ),
        item(
            word = "shame",
            pronunciation = "/ʃeɪm/",
            definitionEn = "a painful feeling of guilt or embarrassment",
            translationFr = "honte",
            translationAr = "عار / خجل",
            exampleEn = "He felt shame.",
            exampleFr = "Il ressentait de la honte.",
            exampleAr = "شعر بالعار."
        ),
        item(
            word = "relief",
            pronunciation = "/rɪˈliːf/",
            definitionEn = "a feeling of happiness when worry ends",
            translationFr = "soulagement",
            translationAr = "ارتياح",
            exampleEn = "He felt great relief.",
            exampleFr = "Il ressentit un grand soulagement.",
            exampleAr = "شعر بارتياح كبير."
        ),
        item(
            word = "relieved",
            pronunciation = "/rɪˈliːvd/",
            definitionEn = "happy because something unpleasant has ended",
            translationFr = "soulagé",
            translationAr = "مرتاح",
            exampleEn = "She was relieved to see him.",
            exampleFr = "Elle fut soulagée de le voir.",
            exampleAr = "شعرت بالارتياح عندما رأته."
        ),
        item(
            word = "surprise",
            pronunciation = "/sərˈpraɪz/",
            definitionEn = "a feeling caused by something unexpected",
            translationFr = "surprise",
            translationAr = "مفاجأة / دهشة",
            exampleEn = "The news caused surprise.",
            exampleFr = "La nouvelle causa de la surprise.",
            exampleAr = "سببت الأخبار الدهشة."
        ),
        item(
            word = "astonished",
            pronunciation = "/əˈstɒnɪʃt/",
            definitionEn = "very surprised",
            translationFr = "étonné",
            translationAr = "مندهش",
            exampleEn = "He was astonished by the news.",
            exampleFr = "Il fut étonné par la nouvelle.",
            exampleAr = "كان مندهشاً من الخبر."
        ),
        item(
            word = "amazed",
            pronunciation = "/əˈmeɪzd/",
            definitionEn = "very surprised and impressed",
            translationFr = "émerveillé / étonné",
            translationAr = "مندهش / مذهول",
            exampleEn = "She was amazed by the scene.",
            exampleFr = "Elle était émerveillée par la scène.",
            exampleAr = "كانت مندهشة من المشهد."
        ),
        item(
            word = "confused",
            pronunciation = "/kənˈfjuːzd/",
            definitionEn = "unable to understand clearly",
            translationFr = "confus",
            translationAr = "مرتبك",
            exampleEn = "He looked confused.",
            exampleFr = "Il avait l'air confus.",
            exampleAr = "بدا مرتبكاً."
        ),
        item(
            word = "curious",
            pronunciation = "/ˈkjʊəriəs/",
            definitionEn = "wanting to know or learn something",
            translationFr = "curieux",
            translationAr = "فضولي",
            exampleEn = "Oliver was curious.",
            exampleFr = "Oliver était curieux.",
            exampleAr = "كان أوليفر فضولياً."
        ),
        item(
            word = "eager",
            pronunciation = "/ˈiːɡər/",
            definitionEn = "very interested and excited to do something",
            translationFr = "impatient / désireux",
            translationAr = "متشوق",
            exampleEn = "He was eager to learn.",
            exampleFr = "Il était désireux d'apprendre.",
            exampleAr = "كان متشوقاً للتعلم."
        ),
        item(
            word = "willing",
            pronunciation = "/ˈwɪlɪŋ/",
            definitionEn = "ready to do something",
            translationFr = "disposé",
            translationAr = "مستعد / راغب",
            exampleEn = "She was willing to help.",
            exampleFr = "Elle était disposée à aider.",
            exampleAr = "كانت مستعدة للمساعدة."
        ),
        item(
            word = "unwilling",
            pronunciation = "/ʌnˈwɪlɪŋ/",
            definitionEn = "not wanting to do something",
            translationFr = "réticent",
            translationAr = "غير راغب",
            exampleEn = "He was unwilling to speak.",
            exampleFr = "Il était réticent à parler.",
            exampleAr = "لم يكن راغباً في الكلام."
        ),
        item(
            word = "lonely",
            pronunciation = "/ˈloʊnli/",
            definitionEn = "sad because you are alone",
            translationFr = "seul / solitaire",
            translationAr = "وحيد",
            exampleEn = "Oliver felt lonely.",
            exampleFr = "Oliver se sentait seul.",
            exampleAr = "شعر أوليفر بالوحدة."
        ),
        item(
            word = "alone",
            pronunciation = "/əˈloʊn/",
            definitionEn = "without other people",
            translationFr = "seul",
            translationAr = "وحده",
            exampleEn = "He was alone in the room.",
            exampleFr = "Il était seul dans la pièce.",
            exampleAr = "كان وحده في الغرفة."
        ),
        item(
            word = "homeless",
            pronunciation = "/ˈhoʊmləs/",
            definitionEn = "without a home",
            translationFr = "sans-abri",
            translationAr = "بلا مأوى",
            exampleEn = "The homeless boy wandered the streets.",
            exampleFr = "Le garçon sans-abri errait dans les rues.",
            exampleAr = "كان الصبي بلا مأوى يتجول في الشوارع."
        ),
        item(
            word = "wandering",
            pronunciation = "/ˈwɒndərɪŋ/",
            definitionEn = "moving without a clear destination",
            translationFr = "errant",
            translationAr = "تائه / يتجول",
            exampleEn = "He was wandering through London.",
            exampleFr = "Il errait dans Londres.",
            exampleAr = "كان يتجول في لندن."
        ),
        item(
            word = "lost",
            pronunciation = "/lɒst/",
            definitionEn = "unable to find the correct place or direction",
            translationFr = "perdu",
            translationAr = "تائه",
            exampleEn = "Oliver was lost.",
            exampleFr = "Oliver était perdu.",
            exampleAr = "كان أوليفر تائهاً."
        ),
        item(
            word = "tremble",
            pronunciation = "/ˈtrembəl/",
            definitionEn = "to shake slightly because of fear or cold",
            translationFr = "trembler",
            translationAr = "يرتجف",
            exampleEn = "His hands trembled.",
            exampleFr = "Ses mains tremblaient.",
            exampleAr = "ارتجفت يداه."
        ),
        item(
            word = "shiver",
            pronunciation = "/ˈʃɪvər/",
            definitionEn = "to shake slightly because of cold or fear",
            translationFr = "frissonner",
            translationAr = "يرتجف",
            exampleEn = "He shivered in the cold.",
            exampleFr = "Il frissonna dans le froid.",
            exampleAr = "ارتجف من البرد."
        ),
        item(
            word = "faint",
            pronunciation = "/feɪnt/",
            definitionEn = "to lose consciousness briefly",
            translationFr = "s'évanouir",
            translationAr = "يغمى عليه",
            exampleEn = "He fainted from weakness.",
            exampleFr = "Il s'évanouit de faiblesse.",
            exampleAr = "أُغمي عليه من الضعف."
        ),
        item(
            word = "conscious",
            pronunciation = "/ˈkɒnʃəs/",
            definitionEn = "awake and aware",
            translationFr = "conscient",
            translationAr = "واعٍ",
            exampleEn = "He was still conscious.",
            exampleFr = "Il était encore conscient.",
            exampleAr = "كان لا يزال واعياً."
        ),
        item(
            word = "breath",
            pronunciation = "/breθ/",
            definitionEn = "air taken into or out of the lungs",
            translationFr = "souffle / respiration",
            translationAr = "نَفَس",
            exampleEn = "He took a deep breath.",
            exampleFr = "Il prit une profonde inspiration.",
            exampleAr = "أخذ نفساً عميقاً."
        ),
        item(
            word = "breathe",
            pronunciation = "/briːð/",
            definitionEn = "to take air into and out of the lungs",
            translationFr = "respirer",
            translationAr = "يتنفس",
            exampleEn = "Try to breathe slowly.",
            exampleFr = "Essaie de respirer lentement.",
            exampleAr = "حاول أن تتنفس ببطء."
        ),
        item(
            word = "voice",
            pronunciation = "/vɔɪs/",
            definitionEn = "the sound made when speaking",
            translationFr = "voix",
            translationAr = "صوت",
            exampleEn = "His voice was quiet.",
            exampleFr = "Sa voix était faible.",
            exampleAr = "كان صوته خافتاً."
        ),
        item(
            word = "sound",
            pronunciation = "/saʊnd/",
            definitionEn = "something that can be heard",
            translationFr = "son",
            translationAr = "صوت",
            exampleEn = "A strange sound came from outside.",
            exampleFr = "Un son étrange venait de dehors.",
            exampleAr = "صدر صوت غريب من الخارج."
        ),
        item(
            word = "look",
            pronunciation = "/lʊk/",
            definitionEn = "to direct your eyes toward something",
            translationFr = "regarder",
            translationAr = "ينظر",
            exampleEn = "Look at the door.",
            exampleFr = "Regarde la porte.",
            exampleAr = "انظر إلى الباب."
        ),
        item(
            word = "stare",
            pronunciation = "/ster/",
            definitionEn = "to look at someone or something for a long time",
            translationFr = "fixer du regard",
            translationAr = "يحدق",
            exampleEn = "They stared at him.",
            exampleFr = "Ils le fixèrent du regard.",
            exampleAr = "حدقوا فيه."
        ),
        item(
            word = "glance",
            pronunciation = "/ɡlæns/",
            definitionEn = "to look quickly",
            translationFr = "jeter un coup d'œil",
            translationAr = "يلقي نظرة سريعة",
            exampleEn = "He glanced at the letter.",
            exampleFr = "Il jeta un coup d'œil à la lettre.",
            exampleAr = "ألقى نظرة سريعة على الرسالة."
        ),
        item(
            word = "notice",
            pronunciation = "/ˈnoʊtɪs/",
            definitionEn = "to become aware of something",
            translationFr = "remarquer",
            translationAr = "يلاحظ",
            exampleEn = "He noticed the mark.",
            exampleFr = "Il remarqua la marque.",
            exampleAr = "لاحظ العلامة."
        ),
        item(
            word = "observe",
            pronunciation = "/əbˈzɜːrv/",
            definitionEn = "to watch carefully",
            translationFr = "observer",
            translationAr = "يراقب",
            exampleEn = "The officer observed him.",
            exampleFr = "L'agent l'observa.",
            exampleAr = "راقبه الضابط."
        ),
        item(
            word = "appear",
            pronunciation = "/əˈpɪər/",
            definitionEn = "to become visible or seem to be",
            translationFr = "apparaître",
            translationAr = "يظهر / يبدو",
            exampleEn = "A man appeared at the door.",
            exampleFr = "Un homme apparut à la porte.",
            exampleAr = "ظهر رجل عند الباب."
        ),
        item(
            word = "disappear",
            pronunciation = "/ˌdɪsəˈpɪər/",
            definitionEn = "to become impossible to see",
            translationFr = "disparaître",
            translationAr = "يختفي",
            exampleEn = "The stranger disappeared.",
            exampleFr = "L'inconnu disparut.",
            exampleAr = "اختفى الغريب."
        ),
        item(
            word = "approach",
            pronunciation = "/əˈproʊtʃ/",
            definitionEn = "to move closer",
            translationFr = "s'approcher",
            translationAr = "يقترب",
            exampleEn = "A man approached Oliver.",
            exampleFr = "Un homme s'approcha d'Oliver.",
            exampleAr = "اقترب رجل من أوليفر."
        ),
        item(
            word = "avoid",
            pronunciation = "/əˈvɔɪd/",
            definitionEn = "to keep away from something",
            translationFr = "éviter",
            translationAr = "يتجنب",
            exampleEn = "He tried to avoid trouble.",
            exampleFr = "Il essaya d'éviter les problèmes.",
            exampleAr = "حاول تجنب المشاكل."
        ),
        item(
            word = "protect",
            pronunciation = "/prəˈtekt/",
            definitionEn = "to keep someone safe from harm",
            translationFr = "protéger",
            translationAr = "يحمي",
            exampleEn = "She protected the child.",
            exampleFr = "Elle protégea l'enfant.",
            exampleAr = "حمَت الطفل."
        ),
        item(
            word = "save",
            pronunciation = "/seɪv/",
            definitionEn = "to protect someone from danger",
            translationFr = "sauver",
            translationAr = "ينقذ",
            exampleEn = "He saved Oliver.",
            exampleFr = "Il sauva Oliver.",
            exampleAr = "أنقذ أوليفر."
        ),
        item(
            word = "rescue",
            pronunciation = "/ˈreskjuː/",
            definitionEn = "to save someone from danger",
            translationFr = "secourir",
            translationAr = "ينقذ",
            exampleEn = "The police rescued him.",
            exampleFr = "La police le secourut.",
            exampleAr = "أنقذته الشرطة."
        ),
        item(
            word = "help",
            pronunciation = "/help/",
            definitionEn = "to make something easier for someone",
            translationFr = "aider",
            translationAr = "يساعد",
            exampleEn = "The gentleman helped Oliver.",
            exampleFr = "Le gentleman aida Oliver.",
            exampleAr = "ساعد الرجل المحترم أوليفر."
        ),
        item(
            word = "support",
            pronunciation = "/səˈpɔːrt/",
            definitionEn = "to help or encourage someone",
            translationFr = "soutenir",
            translationAr = "يدعم",
            exampleEn = "She supported the child.",
            exampleFr = "Elle soutint l'enfant.",
            exampleAr = "دعمت الطفل."
        ),
        item(
            word = "comfort",
            pronunciation = "/ˈkʌmfərt/",
            definitionEn = "to make someone feel less worried or sad",
            translationFr = "réconforter",
            translationAr = "يواسي",
            exampleEn = "She comforted Oliver.",
            exampleFr = "Elle réconforta Oliver.",
            exampleAr = "واسَت أوليفر."
        ),
        item(
            word = "welcome",
            pronunciation = "/ˈwelkəm/",
            definitionEn = "to greet someone gladly",
            translationFr = "accueillir",
            translationAr = "يرحب",
            exampleEn = "They welcomed him.",
            exampleFr = "Ils l'accueillirent.",
            exampleAr = "رحبوا به."
        ),
        item(
            word = "invite",
            pronunciation = "/ɪnˈvaɪt/",
            definitionEn = "to ask someone to come",
            translationFr = "inviter",
            translationAr = "يدعو",
            exampleEn = "She invited him inside.",
            exampleFr = "Elle l'invita à entrer.",
            exampleAr = "دعته إلى الداخل."
        ),
        item(
            word = "offer",
            pronunciation = "/ˈɒfər/",
            definitionEn = "to say that something is available or to give something",
            translationFr = "offrir",
            translationAr = "يعرض / يقدم",
            exampleEn = "He offered food.",
            exampleFr = "Il offrit de la nourriture.",
            exampleAr = "قدم الطعام."
        ),
        item(
            word = "provide",
            pronunciation = "/prəˈvaɪd/",
            definitionEn = "to give something that is needed",
            translationFr = "fournir",
            translationAr = "يوفر",
            exampleEn = "The house provided shelter.",
            exampleFr = "La maison fournissait un abri.",
            exampleAr = "كان المنزل يوفر مأوى."
        ),
        item(
            word = "receive",
            pronunciation = "/rɪˈsiːv/",
            definitionEn = "to get something that is given",
            translationFr = "recevoir",
            translationAr = "يتلقى",
            exampleEn = "He received a letter.",
            exampleFr = "Il reçut une lettre.",
            exampleAr = "تلقى رسالة."
        ),
        item(
            word = "send",
            pronunciation = "/send/",
            definitionEn = "to cause something to go to another place",
            translationFr = "envoyer",
            translationAr = "يرسل",
            exampleEn = "She sent a letter.",
            exampleFr = "Elle envoya une lettre.",
            exampleAr = "أرسلت رسالة."
        ),
        item(
            word = "bring",
            pronunciation = "/brɪŋ/",
            definitionEn = "to take something or someone to a place",
            translationFr = "apporter",
            translationAr = "يجلب",
            exampleEn = "Bring the book here.",
            exampleFr = "Apporte le livre ici.",
            exampleAr = "أحضر الكتاب إلى هنا."
        ),
        item(
            word = "carry",
            pronunciation = "/ˈkæri/",
            definitionEn = "to hold and move something",
            translationFr = "porter",
            translationAr = "يحمل",
            exampleEn = "He carried the bag.",
            exampleFr = "Il porta le sac.",
            exampleAr = "حمل الحقيبة."
        ),
        item(
            word = "hold",
            pronunciation = "/hoʊld/",
            definitionEn = "to have or keep something in your hands",
            translationFr = "tenir",
            translationAr = "يمسك",
            exampleEn = "He held the key.",
            exampleFr = "Il tenait la clé.",
            exampleAr = "أمسك المفتاح."
        ),
        item(
            word = "drop",
            pronunciation = "/drɒp/",
            definitionEn = "to let something fall",
            translationFr = "laisser tomber",
            translationAr = "يسقط / يفلت",
            exampleEn = "He dropped the coin.",
            exampleFr = "Il laissa tomber la pièce.",
            exampleAr = "أسقط القطعة النقدية."
        ),
        item(
            word = "pick",
            pronunciation = "/pɪk/",
            definitionEn = "to take something with the fingers",
            translationFr = "prendre / ramasser",
            translationAr = "يلتقط",
            exampleEn = "He picked up the coin.",
            exampleFr = "Il ramassa la pièce.",
            exampleAr = "التقط القطعة النقدية."
        ),
        item(
            word = "push",
            pronunciation = "/pʊʃ/",
            definitionEn = "to move something away using force",
            translationFr = "pousser",
            translationAr = "يدفع",
            exampleEn = "He pushed the door.",
            exampleFr = "Il poussa la porte.",
            exampleAr = "دفع الباب."
        ),
        item(
            word = "pull",
            pronunciation = "/pʊl/",
            definitionEn = "to move something toward you",
            translationFr = "tirer",
            translationAr = "يسحب",
            exampleEn = "He pulled the rope.",
            exampleFr = "Il tira la corde.",
            exampleAr = "سحب الحبل."
        ),
        item(
            word = "open",
            pronunciation = "/ˈoʊpən/",
            definitionEn = "to move something so an entrance is accessible",
            translationFr = "ouvrir",
            translationAr = "يفتح",
            exampleEn = "Open the door.",
            exampleFr = "Ouvre la porte.",
            exampleAr = "افتح الباب."
        ),
        item(
            word = "close",
            pronunciation = "/kloʊz/",
            definitionEn = "to shut something",
            translationFr = "fermer",
            translationAr = "يغلق",
            exampleEn = "Close the window.",
            exampleFr = "Ferme la fenêtre.",
            exampleAr = "أغلق النافذة."
        ),
        item(
            word = "lock",
            pronunciation = "/lɒk/",
            definitionEn = "to secure something with a lock",
            translationFr = "verrouiller",
            translationAr = "يقفل",
            exampleEn = "They locked the door.",
            exampleFr = "Ils verrouillèrent la porte.",
            exampleAr = "أقفلوا الباب."
        ),
        item(
            word = "unlock",
            pronunciation = "/ʌnˈlɒk/",
            definitionEn = "to open something that was locked",
            translationFr = "déverrouiller",
            translationAr = "يفتح القفل",
            exampleEn = "He unlocked the door.",
            exampleFr = "Il déverrouilla la porte.",
            exampleAr = "فتح قفل الباب."
        ),
        item(
            word = "sit",
            pronunciation = "/sɪt/",
            definitionEn = "to rest on a chair or surface",
            translationFr = "s'asseoir",
            translationAr = "يجلس",
            exampleEn = "Sit here.",
            exampleFr = "Assieds-toi ici.",
            exampleAr = "اجلس هنا."
        ),
        item(
            word = "stand",
            pronunciation = "/stænd/",
            definitionEn = "to be upright on your feet",
            translationFr = "se tenir debout",
            translationAr = "يقف",
            exampleEn = "He stood by the door.",
            exampleFr = "Il se tenait près de la porte.",
            exampleAr = "وقف قرب الباب."
        ),
        item(
            word = "sleep",
            pronunciation = "/sliːp/",
            definitionEn = "to rest with your eyes closed",
            translationFr = "dormir",
            translationAr = "ينام",
            exampleEn = "The child slept quietly.",
            exampleFr = "L'enfant dormit paisiblement.",
            exampleAr = "نام الطفل بهدوء."
        ),
        item(
            word = "wake",
            pronunciation = "/weɪk/",
            definitionEn = "to stop sleeping",
            translationFr = "se réveiller",
            translationAr = "يستيقظ",
            exampleEn = "He woke early.",
            exampleFr = "Il se réveilla tôt.",
            exampleAr = "استيقظ مبكراً."
        ),
        item(
            word = "dream",
            pronunciation = "/driːm/",
            definitionEn = "a series of thoughts or images during sleep",
            translationFr = "rêver / rêve",
            translationAr = "يحلم / حلم",
            exampleEn = "Oliver dreamed of a better life.",
            exampleFr = "Oliver rêvait d'une vie meilleure.",
            exampleAr = "حلم أوليفر بحياة أفضل."
        ),
        item(
            word = "die",
            pronunciation = "/daɪ/",
            definitionEn = "to stop living",
            translationFr = "mourir",
            translationAr = "يموت",
            exampleEn = "Many children died young.",
            exampleFr = "Beaucoup d'enfants moururent jeunes.",
            exampleAr = "مات كثير من الأطفال صغاراً."
        ),
        item(
            word = "death",
            pronunciation = "/deθ/",
            definitionEn = "the end of life",
            translationFr = "mort",
            translationAr = "موت",
            exampleEn = "His death was a tragedy.",
            exampleFr = "Sa mort fut une tragédie.",
            exampleAr = "كانت وفاته مأساة."
        ),
        item(
            word = "dead",
            pronunciation = "/ded/",
            definitionEn = "no longer alive",
            translationFr = "mort",
            translationAr = "ميت",
            exampleEn = "The man was dead.",
            exampleFr = "L'homme était mort.",
            exampleAr = "كان الرجل ميتاً."
        ),
        item(
            word = "life",
            pronunciation = "/laɪf/",
            definitionEn = "the state of being alive",
            translationFr = "vie",
            translationAr = "حياة",
            exampleEn = "He wanted a better life.",
            exampleFr = "Il voulait une vie meilleure.",
            exampleAr = "أراد حياة أفضل."
        ),
        item(
            word = "alive",
            pronunciation = "/əˈlaɪv/",
            definitionEn = "living, not dead",
            translationFr = "vivant",
            translationAr = "حي",
            exampleEn = "Oliver was still alive.",
            exampleFr = "Oliver était encore vivant.",
            exampleAr = "كان أوليفر لا يزال حياً."
        ),
        item(
            word = "born",
            pronunciation = "/bɔːrn/",
            definitionEn = "brought into existence at birth",
            translationFr = "né",
            translationAr = "مولود",
            exampleEn = "Oliver was born in a workhouse.",
            exampleFr = "Oliver est né dans une maison de travail.",
            exampleAr = "وُلد أوليفر في دار للفقراء."
        ),
        item(
            word = "grow",
            pronunciation = "/ɡroʊ/",
            definitionEn = "to become larger or older",
            translationFr = "grandir",
            translationAr = "يكبر / ينمو",
            exampleEn = "Children grow quickly.",
            exampleFr = "Les enfants grandissent vite.",
            exampleAr = "ينمو الأطفال بسرعة."
        ),
        item(
            word = "change",
            pronunciation = "/tʃeɪndʒ/",
            definitionEn = "to become different",
            translationFr = "changer",
            translationAr = "يتغير",
            exampleEn = "His life changed.",
            exampleFr = "Sa vie changea.",
            exampleAr = "تغيرت حياته."
        ),
        item(
            word = "begin",
            pronunciation = "/bɪˈɡɪn/",
            definitionEn = "to start",
            translationFr = "commencer",
            translationAr = "يبدأ",
            exampleEn = "The story begins here.",
            exampleFr = "L'histoire commence ici.",
            exampleAr = "تبدأ القصة هنا."
        ),
        item(
            word = "continue",
            pronunciation = "/kənˈtɪnjuː/",
            definitionEn = "to keep doing something",
            translationFr = "continuer",
            translationAr = "يواصل",
            exampleEn = "He continued walking.",
            exampleFr = "Il continua à marcher.",
            exampleAr = "واصل المشي."
        ),
        item(
            word = "finish",
            pronunciation = "/ˈfɪnɪʃ/",
            definitionEn = "to complete something",
            translationFr = "finir",
            translationAr = "ينهي",
            exampleEn = "He finished the meal.",
            exampleFr = "Il termina le repas.",
            exampleAr = "أنهى الوجبة."
        ),
        item(
            word = "decide",
            pronunciation = "/dɪˈsaɪd/",
            definitionEn = "to choose after thinking",
            translationFr = "décider",
            translationAr = "يقرر",
            exampleEn = "He decided to leave.",
            exampleFr = "Il décida de partir.",
            exampleAr = "قرر المغادرة."
        ),
        item(
            word = "choose",
            pronunciation = "/tʃuːz/",
            definitionEn = "to select one thing from several",
            translationFr = "choisir",
            translationAr = "يختار",
            exampleEn = "He chose the safer road.",
            exampleFr = "Il choisit la route la plus sûre.",
            exampleAr = "اختار الطريق الأكثر أماناً."
        ),
        item(
            word = "try",
            pronunciation = "/traɪ/",
            definitionEn = "to attempt to do something",
            translationFr = "essayer",
            translationAr = "يحاول",
            exampleEn = "He tried to escape.",
            exampleFr = "Il essaya de s'échapper.",
            exampleAr = "حاول الهرب."
        ),
        item(
            word = "manage",
            pronunciation = "/ˈmænɪdʒ/",
            definitionEn = "to succeed in doing something",
            translationFr = "réussir à",
            translationAr = "يتمكن من",
            exampleEn = "He managed to escape.",
            exampleFr = "Il réussit à s'échapper.",
            exampleAr = "تمكن من الهرب."
        ),
        item(
            word = "fail",
            pronunciation = "/feɪl/",
            definitionEn = "to not succeed",
            translationFr = "échouer",
            translationAr = "يفشل",
            exampleEn = "He failed to escape.",
            exampleFr = "Il échoua à s'échapper.",
            exampleAr = "فشل في الهرب."
        ),
        item(
            word = "learn",
            pronunciation = "/lɜːrn/",
            definitionEn = "to gain knowledge or skill",
            translationFr = "apprendre",
            translationAr = "يتعلم",
            exampleEn = "He learned a lesson.",
            exampleFr = "Il apprit une leçon.",
            exampleAr = "تعلم درساً."
        ),
        item(
            word = "teach",
            pronunciation = "/tiːtʃ/",
            definitionEn = "to give knowledge or skills",
            translationFr = "enseigner",
            translationAr = "يعلّم",
            exampleEn = "They taught him to read.",
            exampleFr = "Ils lui apprirent à lire.",
            exampleAr = "علموه القراءة."
        ),
        item(
            word = "read",
            pronunciation = "/riːd/",
            definitionEn = "to look at and understand written words",
            translationFr = "lire",
            translationAr = "يقرأ",
            exampleEn = "Oliver learned to read.",
            exampleFr = "Oliver apprit à lire.",
            exampleAr = "تعلم أوليفر القراءة."
        ),
        item(
            word = "write",
            pronunciation = "/raɪt/",
            definitionEn = "to form words on paper or a screen",
            translationFr = "écrire",
            translationAr = "يكتب",
            exampleEn = "He wrote a letter.",
            exampleFr = "Il écrivit une lettre.",
            exampleAr = "كتب رسالة."
        ),
        item(
            word = "letter",
            pronunciation = "/ˈletər/",
            definitionEn = "a written message sent to someone",
            translationFr = "lettre",
            translationAr = "رسالة",
            exampleEn = "He received a letter.",
            exampleFr = "Il reçut une lettre.",
            exampleAr = "تلقى رسالة."
        ),
        item(
            word = "message",
            pronunciation = "/ˈmesɪdʒ/",
            definitionEn = "information sent to someone",
            translationFr = "message",
            translationAr = "رسالة / خبر",
            exampleEn = "The message was important.",
            exampleFr = "Le message était important.",
            exampleAr = "كانت الرسالة مهمة."
        ),
        item(
            word = "book",
            pronunciation = "/bʊk/",
            definitionEn = "a written work made of pages",
            translationFr = "livre",
            translationAr = "كتاب",
            exampleEn = "He opened the book.",
            exampleFr = "Il ouvrit le livre.",
            exampleAr = "فتح الكتاب."
        ),
        item(
            word = "story",
            pronunciation = "/ˈstɔːri/",
            definitionEn = "a narrative about people or events",
            translationFr = "histoire / récit",
            translationAr = "قصة",
            exampleEn = "The story was unforgettable.",
            exampleFr = "L'histoire était inoubliable.",
            exampleAr = "كانت القصة لا تُنسى."
        ),
        item(
            word = "chapter",
            pronunciation = "/ˈtʃæptər/",
            definitionEn = "a main division of a book",
            translationFr = "chapitre",
            translationAr = "فصل",
            exampleEn = "We read the first chapter.",
            exampleFr = "Nous lisons le premier chapitre.",
            exampleAr = "نقرأ الفصل الأول."
        ),
        item(
            word = "scene",
            pronunciation = "/siːn/",
            definitionEn = "a part of a story or event",
            translationFr = "scène",
            translationAr = "مشهد",
            exampleEn = "The scene takes place in London.",
            exampleFr = "La scène se déroule à Londres.",
            exampleAr = "تدور أحداث المشهد في لندن."
        ),
        item(
            word = "character",
            pronunciation = "/ˈkærəktər/",
            definitionEn = "a person in a story",
            translationFr = "personnage",
            translationAr = "شخصية",
            exampleEn = "Oliver is the main character.",
            exampleFr = "Oliver est le personnage principal.",
            exampleAr = "أوليفر هو الشخصية الرئيسية."
        ),
        item(
            word = "narrator",
            pronunciation = "/nəˈreɪtər/",
            definitionEn = "the person or voice that tells a story",
            translationFr = "narrateur",
            translationAr = "الراوي",
            exampleEn = "The narrator describes the scene.",
            exampleFr = "Le narrateur décrit la scène.",
            exampleAr = "يصف الراوي المشهد."
        ),
        item(
            word = "author",
            pronunciation = "/ˈɔːθər/",
            definitionEn = "the person who writes a book",
            translationFr = "auteur",
            translationAr = "مؤلف",
            exampleEn = "Dickens was the author.",
            exampleFr = "Dickens était l'auteur.",
            exampleAr = "كان ديكنز هو المؤلف."
        ),
        item(
            word = "novel",
            pronunciation = "/ˈnɒvəl/",
            definitionEn = "a long fictional story",
            translationFr = "roman",
            translationAr = "رواية",
            exampleEn = "Oliver Twist is a novel.",
            exampleFr = "Oliver Twist est un roman.",
            exampleAr = "أوليفر تويست رواية."
        ),
        item(
            word = "literature",
            pronunciation = "/ˈlɪtərətʃər/",
            definitionEn = "written works considered valuable as art",
            translationFr = "littérature",
            translationAr = "أدب",
            exampleEn = "The novel is part of English literature.",
            exampleFr = "Le roman fait partie de la littérature anglaise.",
            exampleAr = "الرواية جزء من الأدب الإنجليزي."
        ),
        item(
            word = "chapter title",
            pronunciation = "/ˈtʃæptər ˌtaɪtəl/",
            definitionEn = "the title of a chapter",
            translationFr = "titre du chapitre",
            translationAr = "عنوان الفصل",
            exampleEn = "Read the chapter title first.",
            exampleFr = "Lisez d'abord le titre du chapitre.",
            exampleAr = "اقرأ عنوان الفصل أولاً."
        ),
        item(
            word = "meaning",
            pronunciation = "/ˈmiːnɪŋ/",
            definitionEn = "what a word or idea expresses",
            translationFr = "sens",
            translationAr = "معنى",
            exampleEn = "What is the meaning of this word?",
            exampleFr = "Quel est le sens de ce mot ?",
            exampleAr = "ما معنى هذه الكلمة؟"
        ),
        item(
            word = "vocabulary",
            pronunciation = "/vəˈkæbjələri/",
            definitionEn = "the words known or used by a person or text",
            translationFr = "vocabulaire",
            translationAr = "مفردات",
            exampleEn = "The novel has rich vocabulary.",
            exampleFr = "Le roman possède un vocabulaire riche.",
            exampleAr = "تحتوي الرواية على مفردات غنية."
        )
    )

    private val normalizedWordMap: Map<String, OliverVocabularyItem> by lazy {
        all.associateBy { normalize(it.word) }
    }

    /**
     * Recherche exacte ou partielle dans toute la banque.
     */
    fun search(query: String): List<OliverVocabularyItem> {
        val q = normalize(query)
        if (q.isBlank()) return all
        return all.filter {
            normalize(it.word).contains(q) ||
                    normalize(it.translationFr).contains(q) ||
                    it.translationAr.contains(q)
        }
    }

    /**
     * Retourne une fiche précise par son mot anglais.
     */
    fun findWord(word: String): OliverVocabularyItem? {
        return normalizedWordMap[normalize(word)]
    }

    /**
     * Extrait les mots de la banque réellement présents dans un texte/page.
     * L'ordre suit l'ordre d'apparition dans le texte.
     */
    fun extractForText(
        text: String,
        maxWords: Int = 30
    ): List<OliverVocabularyItem> {
        if (text.isBlank() || maxWords <= 0) return emptyList()

        val normalizedText = normalize(text)
        val matches = mutableListOf<OliverVocabularyItem>()

        all.forEach { item ->
            if (matches.size >= maxWords) return@forEach

            val normalizedWord = normalize(item.word)
            if (normalizedWord.isBlank()) return@forEach

            val regex = Regex(
                "(?<![a-z])" +
                        Regex.escape(normalizedWord) +
                        "(?![a-z])"
            )

            if (regex.containsMatchIn(normalizedText)) {
                matches += item
            }
        }

        return matches.sortedBy {
            val position = normalizedText.indexOf(normalize(it.word))
            if (position < 0) Int.MAX_VALUE else position
        }.take(maxWords)
    }

    /**
     * Retourne les N premiers mots de la banque.
     */
    fun first(count: Int = 30): List<OliverVocabularyItem> =
        all.take(count.coerceAtLeast(0))

    private fun normalize(value: String): String {
        return value
            .lowercase()
            .replace('’', '\'')
            .replace(Regex("[^a-z0-9']+"), " ")
            .trim()
    }
}
