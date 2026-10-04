package ma.fldm.englishstudies.updates

data class UpdateInfo(
    val versionCode: Int,
    val versionName: String,
    val downloadUrl: String,
    val changelog: String,
    val mandatory: Boolean
)
