# CS 340 Portfolio Item: Grazioso Salvare Dashboard

**Student:** Shaban Ghaith  
**Course:** CS 340 Client/Server Development  
**Portfolio Artifact:** Project Two Dashboard and README

## Project Overview

This repository contains my CS 340 Project Two dashboard for Grazioso Salvare. The dashboard uses animal shelter data from the Austin Animal Center and lets the user filter possible rescue dog candidates by rescue type. It includes the dashboard notebook, the MongoDB CRUD module, the original Project Two README Word document, the logo image, the data file and the requirements file.

## Files Included

- `ProjectTwoDashboard.ipynb`: Final dashboard code.
- `CRUD_Python_Module.py`: CRUD module used by the dashboard notebook.
- `animal_shelter.py`: Additional CRUD module version from the project package.
- `CS340_Project_Two_README_Shaban_Ghaith.docx`: Project Two README Word document.
- `Grazioso Salvare Logo.png`: Logo used in the dashboard.
- `aac_shelter_outcomes.csv`: Data file used as a fallback if MongoDB is not available.
- `requirements.txt`: Python packages needed for the dashboard.

## Reflection

### How do you write programs that are maintainable, readable and adaptable?

I try to write programs by keeping the code organized into pieces that each have a clear job. In Project One, the CRUD Python module helped with this because the database connection and create, read, update and delete functions were separated from the dashboard code. Then in Project Two, the dashboard could focus more on the user interface, filters, map and charts instead of repeating database logic everywhere.

The biggest advantage of working this way was that the code was easier to reuse and update. If the database connection or query logic needed to change, I could work mostly in the CRUD module instead of searching through the whole dashboard. In the future, I could reuse this CRUD module for another MongoDB project that needs to connect to a collection, run queries, or support a dashboard or web app.

### How do you approach a problem as a computer scientist?

For this project, I started by looking at what Grazioso Salvare needed from the dashboard. They needed a way to filter shelter animals based on rescue training categories, so I broke the problem into smaller parts: connecting to MongoDB, reading the animal data, applying the correct filter rules and displaying the results in a table, chart and map.

This project felt different from earlier assignments because it was closer to a real client request. Instead of only writing code to pass a small test, I had to think about whether the dashboard would actually help someone understand the data. In the future, I would use the same basic strategy for database projects: understand the client's goal first, review the data fields, design queries around the actual business need and test the results to make sure the output is useful.

### What do computer scientists do and why does it matter?

Computer scientists solve problems by designing systems that organize, process and present information in useful ways. This matters because companies often have a lot of data, but the data is not helpful unless people can actually search it, filter it and make decisions from it.

For a company like Grazioso Salvare, this type of dashboard could help staff quickly find animals that match certain rescue training profiles. Instead of manually reading through shelter records, they can use the dashboard to narrow the data and focus on strong candidates. That can save time, reduce mistakes and help the organization make better decisions.

## AI Usage Acknowledgment

I used ChatGPT to understand the assignment and get help with writing, code and debugging. I made the final decisions about the design, goals, learning and final work.
