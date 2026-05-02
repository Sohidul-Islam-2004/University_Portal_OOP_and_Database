# 🎓 University Portal System

A complete **University Management System** that streamlines academic operations through an intuitive command-line interface. Built with **Object-Oriented Programming (OOP)** principles and **MySQL** database integration.

## ✨ Features

### 👨‍🎓 Student Portal
- **Account Management**: Register, login with security verification
- **Profile Management**: View and edit personal information
- **Course Management**: Browse available courses, enroll/withdraw
- **Academic Records**: View results, calculate CGPA automatically
- **Grade Analysis**: Track semester-wise performance

### 👨‍🏫 Faculty Portal
- **Profile Management**: Complete faculty profile with position details
- **Student Advising**: Advise students on course selection
- **Grade Management**: Enter marks, automatic grade calculation
- **Student Oversight**: View student profiles, results, and progress
- **Advisor Dashboard**: Monitor assigned student list


## Tech Stack

- Java 17+
- MySQL 8.0+
- JDBC for database connectivity

---

## Database Setup Guide

### Step 1: Install MySQL

Download and install MySQL 8.0+ from https://dev.mysql.com/downloads/

### Step 2: Import Database using MySQL Workbench

1. **Open MySQL Workbench**
2. **Connect to your local instance** (click on "Local instance MySQL")
3. **Open the SQL file**: File → Open SQL Script (Ctrl+Shift+O)
4. **Select** `SQL.sql` from your downloaded files
5. **Execute the script**: Click the lightning bolt icon (or press Ctrl+Shift+Enter)
6. **Wait** for all queries to complete (you'll see green checkmarks)
7. **Refresh**: Right-click in Schemas panel → Refresh All

You should now see `University_Portal` database in the Schemas list.

