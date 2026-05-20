cmake_minimum_required(VERSION 3.28.1)
project(untitled C)

set(CMAKE_C_STANDARD 99)
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Os -s")

add_executable(untitled main.c)
