# MovieFlix – Movie Search Engine

## Project Overview

MovieFlix is a Java-based movie search engine developed as an Advanced Algorithms / DSA project.

The system reads movie information from multiple text files stored in the `docs` folder and provides different searching and similarity operations using string matching, dynamic programming, and text-processing algorithms.

The project is designed to demonstrate the practical application of advanced algorithms on a movie corpus.

---

## Features

MovieFlix provides the following main features:

1. **KMP Pattern Search**
   - Searches for a given pattern in the movie corpus.
   - Uses the Knuth-Morris-Pratt (KMP) string matching algorithm.
   - Avoids unnecessary character comparisons using the LPS array.

2. **Aho-Corasick Multi-Keyword Search**
   - Searches for multiple keywords simultaneously.
   - Uses a trie with failure links.
   - Useful for searching several patterns in the movie corpus.

3. **Fuzzy Movie Search**
   - Finds movies with similar text or titles.
   - Uses the Levenshtein edit-distance algorithm.
   - Measures the number of insertions, deletions, and substitutions required to transform one string into another.

4. **Movie Similarity**
   - Compares movie information using text-based similarity.
   - Uses TF-IDF and cosine similarity.
   - Helps identify movies with similar keywords and descriptions.

5. **Movie Corpus Processing**
   - Reads multiple `.txt` files from the `docs` folder.
   - Parses movie records into structured `MovieRecord` objects.
   - Supports a large number of movie records.

---

## Technologies Used

- Java
- Java Collections / Standard Java Libraries
- File Handling
- String Algorithms
- Dynamic Programming
- Text Processing

---

## Project Structure

```text
KLH_CSE_2025-26_S9_8_MovieFlix-main
│
├── docs
│   ├── 1.txt
│   ├── 2.txt
│   ├── 3.txt
│   ├── ...
│   └── movie corpus files
│
├── corpus
│
├── src
│   ├── AhoCorasick.java
│   ├── FileReaderUtil.java
│   ├── KMP.java
│   ├── Levenshtein.java
│   ├── MovieFlix.java
│   ├── MovieRecord.java
│   └── TFIDFCosine.java
│
├── out
│   └── Compiled Java class files
│
├── run.bat
│
└── README.md
