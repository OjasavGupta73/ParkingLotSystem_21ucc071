# Parking Lot System

## Overview

This is a Parking Lot System implemented in Java with concurrency handling. It efficiently manages parking spots for different vehicle types while ensuring synchronization across multiple threads.

## Features

- Supports Cars, Bikes, and Trucks with different space allocations.
- Concurrency handling using locks and conditions to ensure thread safety.
- Optimized parking: Cars are parked based on the distance from the entrance to minimize travel time.
- Ticket ID system: Used to ease the process of finding and unparking vehicles.
- License plate uniqueness: Prevents duplicate license plates from being parked.
- Command-line interface for user interaction.

## Technologies Used

- Java (for core logic and OOP design)
- Concurrency (Locks & Conditions) for thread synchronization
- Collections Framework for efficient data handling

## How to Run

1. Ensure you have Java installed on your system.
2. Run the Main class to start the parking system:
    ```sh
    java Main
    ```
3. Follow the on-screen instructions to park and unpark vehicles.

## Future Enhancements

- GUI-based interface
- Database integration for persistent storage
- Dynamic pricing based on parking duration

## Author

Developed by Ojasav Gupta

## License

```
© 2025 Ojasav Gupta. All rights reserved.
```
