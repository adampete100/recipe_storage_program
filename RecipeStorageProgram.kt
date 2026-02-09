import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

//data class for recipe instructions (removed @Serializable)
data class RecipeInstructions(
    val time: String,
    val ingredients: List<String>,
    val steps: List<String>
)

//data class for the full recipe (removed @Serializable)
data class FullRecipe(
    val title: String,
    val author: String,
    val instructions: RecipeInstructions,
    val date: String
)

//--------------------------------main section
//main function for the recipe storage program
fun main() {
    println("Hello! What would you like to do? ")
    //loop for menu options is input is invalid
    while(true) {
        println("\n1. View recipes\n2. Add recipe\n3. Delete recipe\n4. Edit recipe\n5. Exit")
        val mainSelect = readlnOrNull()

        when (mainSelect) {
            "1" -> viewRecipes()
            "2" -> addRecipe()
            "3" -> deleteRecipe()
            "4" -> editRecipe()
            "5" -> break //exit the program loop
            else -> println("Invalid choice, please select option 1-5")
        }
    }
}

//-------------recipe displaying section
//function for displaying stored recipes
fun viewRecipes() {
    val folder = File("recipes")
    if (!folder.exists() || folder.listFiles()?.isEmpty() == true) {
        println("No recipes found to view.")
        return
    }

    val files = folder.listFiles()?.filter { it.extension == "json" } ?: emptyList()
    println("Available Recipes:")
    files.forEachIndexed { index, file -> println("${index + 1}. ${file.nameWithoutExtension.removePrefix("recipe_")}") }

    println("\nEnter the number of the recipe to view (or '0' to go back):")
    val choice = readlnOrNull()?.toIntOrNull() ?: 0
    if (choice in 1..files.size) {
        val selectedFile = files[choice - 1]
        val content = selectedFile.readText()

        // Manually parse the clean data from the JSON string
        val title = content.substringAfter("\"title\": \"").substringBefore("\",")
        val author = content.substringAfter("\"author\": \"").substringBefore("\",")
        val date = content.substringAfter("\"date\": \"").substringBefore("\",")
        val time = content.substringAfter("\"time\": \"").substringBefore("\",")

        // Complex parsing for lists (ingredients and steps)
        val ingredientsString = content.substringAfter("\"ingredients\": [").substringBefore("],")
        val ingredients = ingredientsString.split("\", \"").map { it.trim('"') }

        val stepsString = content.substringAfter("\"steps\": [").substringBefore("]\n")
        val steps = stepsString.split("\", \"").map { it.trim('"') }

        // Print the data in a clean format
        println("\n--- $title ---")
        println("By: $author | Created: $date | Cook Time: $time")

        println("\n--- Ingredients ---")
        ingredients.forEach { println(it) }

        println("\n--- Steps ---")
        steps.forEach { println(it) }

        println("---------------------------\n")

    }
}

//------recipe editing section
//function for editing recipes
fun editRecipe() {
    println("Select a recipe to edit (follows the 'Add' process to overwrite):")
    val folder = File("recipes")
    if (!folder.exists() || folder.listFiles()?.isEmpty() == true) {
        println("No recipes to edit.")
        return
    }
    //re-using addRecipe logic for manual editing as discussed
    addRecipe()
}

//helper function to save edited data
fun saveEditedRecipe(title: String, author: String, instructions: RecipeInstructions, date: String) {
    val recipe = FullRecipe(title, author, instructions, "$date (edited)")
    val manualJson = """
    {
      "title": "${recipe.title}",
      "author": "${recipe.author}",
      "date": "${recipe.date}",
      "instructions": {
        "time": "${recipe.instructions.time}",
        "ingredients": ${recipe.instructions.ingredients.map { "\"$it\"" }},
        "steps": ${recipe.instructions.steps.map { "\"$it\"" }}
      }
    }
    """.trimIndent()

    val safeTitle = title.replace(" ", "_")
    File("recipes", "recipe_$safeTitle.json").writeText(manualJson)
}

//------recipe deletion section
//function for deleting recipes
fun deleteRecipe() {
    val folder = File("recipes")
    val files = folder.listFiles()?.filter { it.extension == "json" } ?: emptyList()

    if (files.isEmpty()) {
        println("Nothing to delete.")
        return
    }

    // Use removePrefix here too for cleaner output
    files.forEachIndexed { index, file -> println("${index + 1}. ${file.nameWithoutExtension.removePrefix("recipe_")}") }
    println("Enter the number to delete (or '0' to cancel):")
    val choice = readlnOrNull()?.toIntOrNull() ?: 0

    if (choice in 1..files.size) {
        if (files[choice - 1].delete()) println("Deleted.") else println("Error deleting.")
    }
}

//---------------------------------------add function section
/*function responsible for calling functions that collect information on the recipe,
and storing recipe data in a file*/
fun addRecipe() {
    val title = addTitle()
    val author = addAuthor()
    val instructions = addInstructions()
    val date = addDate()

    val recipe = FullRecipe(title, author, instructions, date)
    val manualJson = """
    {
      "title": "${recipe.title}",
      "author": "${recipe.author}",
      "date": "${recipe.date}",
      "instructions": {
        "time": "${recipe.instructions.time}",
        "ingredients": ${recipe.instructions.ingredients.map { "\"$it\"" }},
        "steps": ${recipe.instructions.steps.map { "\"$it\"" }}
      }
    }
    """.trimIndent()

    val folder = File("recipes").apply { if (!exists()) mkdirs() }
    val safeTitle = title.replace(" ", "_")
    File(folder, "recipe_$safeTitle.json").writeText(manualJson)
    println("Recipe saved!")
}

//function for getting cooking instructions as input
fun addInstructions(): RecipeInstructions {
    //get cooking time
    println("Enter recipe cook time (write as '_hrs _mins'): ")
    val cookTime = readlnOrNull() ?: "0mins"

    //collect ingredients using the new reusable function
    val ingredients = collectListInput("Enter ingredient (type 'exit' to finish): ", stepPrefix = false)

    //collect steps using the new reusable function with numbering
    val stepsList = collectListInput("Enter step (type 'exit' to finish): ", stepPrefix = true)

    return RecipeInstructions(cookTime, ingredients, stepsList)
}

//helper function to collect a list of strings from user input
fun collectListInput(promptMessage: String, stepPrefix: Boolean): List<String> {
    val list = mutableListOf<String>()
    var count = 1
    while (true) {
        println(promptMessage)
        val input = readln().trim()
        if (input.lowercase() == "exit") break
        if (input.isNotEmpty()) {
            if (stepPrefix) {
                list.add("$count. $input")
                count++
            } else {
                list.add(input)
                println("Current list: $list")
            }
        }
    }
    return list
}

//function responsible for recording date/time of recipe creation
fun addDate(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

//function responsible for getting author's name as input
fun addAuthor(): String {
    println("Enter Author's name: ")
    return readln().ifBlank { "unknown" }
}

//store user title for recipe
fun addTitle(): String {
    println("Enter recipe title: ")
    return readln().ifBlank { "untitled_recipe" }
}
