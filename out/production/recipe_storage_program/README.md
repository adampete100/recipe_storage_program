# GitHub Link

https://github.com/adampete100/recipe_storage_program


# Overview 

My goal in building this project was to gain a better understanding of mobile development languages. 
Building this project allowed me to explore the basics of functional programming, null safety, 
and file CRUD operations within Kotlin.

I developed a Recipe Management System—a command-line interface (CLI) application that allows users 
to create, view, edit, and delete recipes. It does this through the creation, reading, and writing of JSON files.

The purpose of writing this software was to gain hands-on experience with Kotlin’s syntax, mainly 
focusing on how to create functions, and how it handles the `when` expression for control flow.
This project also challenged me to implement manual data serialization and file management logic.


[Software Demo Video](http://youtube.link.goes.here)


# Development Environment

To develop this software, I used **IntelliJ IDEA**, which provides native support for the Kotlin compiler 
and excellent debugging tools for JVM applications.

The project is written in **Kotlin**. I utilized the following standard libraries:
*   `java.io.File`: For managing directory creation and local JSON file persistence.
*   `java.time`: For generating dynamic timestamps for recipes once they're created.
*   `kotlin.collections`: To leverage high-level functions like `.filter`, `.map`, and `.forEach`.


# Useful Websites

- [Kotlin Documentation](https://kotlinlang.org)
- [Programiz Kotlin Tutorial](https://www.programiz.com/kotlin-programming)
- [Kotlin by Example](https://play.kotlinlang.org)


# Future Work

- **Search and Filter Engine:** Add the ability to search recipes by specific ingredients, author,
or cooking time ranges rather than just selecting from a numbered list.
- **Improved Edit Logic:** Update the `editRecipe` function to allow partial updates 
(e.g., changing just the ingredients) without requiring a full re-entry of data.
- **Input Validation:** Implement more rigorous error handling to catch non-integer inputs and prevent 
index-out-of-bounds exceptions when selecting recipes.