import models.*

class DiffRunnerTest {

    private val project = Project(
        name = "Test Project",
        translations = generateTestTranslations()
    )



    private fun generateTestTranslations(): List<Translation> {
        return listOf(
            Translation(
                key = "key_1",
                resources = mapOf(
                    Platform.Android to StringResource(
                        identifier = "android_string_1",
                        localizedValues = mapOf(
                            Language.English to LocalizedValue(
                                Language.English, "android portuguese string 1"
                            )
                        )
                    )
                ),
                module = Module("test_module"),
                section = Section("section_1")
            )
        )
    }
}