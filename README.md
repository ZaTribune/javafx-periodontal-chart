<div align="center">

# 🦷 JavaFX Periodontal Chart

![Java](https://img.shields.io/badge/Java-21+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-23+-4B8BBE?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A comprehensive desktop application for dental professionals to record, track, and visualize periodontal health metrics with precision and ease.**

</div>

## Technology Stack
- **Language**: Java
- **GUI Framework**: JavaFX
- **Build Tool**: Maven
- **Additional Libraries**: JFoenix, ControlsFX, Jackson, JFXtras

---

## Features

### Tooth Data Tracking (Per Tooth)
Each of the 32 teeth tracks:
- **Availability**: Toggle tooth available/unavailable
- **Implant Status**: Mark if tooth is an implant
- **Mobility**: Scale from -3 to +3 (indicating tooth mobility level)
- **Furcation (Fork)**: 3 levels (0-3) for different teeth
- **Bleeding on Probing (BOP)**: 3 measurement points per tooth (0-2 scale)
- **Plaque**: 6 measurement points (0-1 binary scale)
- **Gum Width**: Slider value 50-200
- **Gingival Margin**: 6 text input fields (ranging from -10 to +10)
- **Probing Depth**: 6 text input fields (ranging from -10 to +10)
- **Selected Circle**: Tracking which circle is selected on the chart visualization

### Visualization
- **Dental Images**: Upper and lower views of each tooth
    - Normal tooth, cross (unavailable), implant variants
- **Gingival Margin & Probing Depth**: Graphical line chart overlaid on tooth images
    - Red circles/path for gingival margin
    - Blue circles/path for probing depth (margin + depth)
- **Fork Visualization**: Moon phases (new, quarter, full) representing furcation levels

<p align="center">
  <img src="screenshots/1.jpg" height="250"/>
  <img src="screenshots/2.jpg" height="250"/>
</p>

## How to Run

To run the application, ensure you have **Java 21+** and **Maven** installed, then execute:

```bash
mvn javafx:run
```

This command will compile the project and launch the dental chart application.  

---

## Acknowledgments   
   Thanks to [alejo8591](https://github.com/alejo8591/periodontal-chart) , As I used the dental photos that he provided, as well as the chart mechanisms and functionaities.
   
## Authors
[![Linkedin](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white&label=Muhammad%20Ali)](https://linkedin.com/in/zatribune)
