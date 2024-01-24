"""
    CS051P Lab Assignments: Final Project

    Authors: Dylan O'Connor and Adrian Clement

    Date:   December 9th, 2022

    The goal of this assignment is to visualize the correlation between song popularity
    and a series of factors, being length, key, and genre
"""
import csv
from statistics import mean
import matplotlib.pyplot as plt
from string import whitespace


def parse_song_length(fname):
    """
    Makes a list of song lengths in ms
    :param fname: (file) csv file
    :return: (list) a list of song lengths
    """
    # opens file and reads it
    file1 = open(fname, "r", encoding='utf-8')
    reading = csv.reader(file1)
    next(reading)

    song_length_list = []

    # write into list when certain characters are found (also addresses indexing bug)
    for line in file1:
        count = 0
        duration = ""
        for i in range(len(line)):
            if line[i] == "," and line[i + 1] != " ":
                count += 1
            elif count == 2 and line[i] != ",":
                duration += line[i]
        song_length_list.append(duration)

    # returns final list
    return song_length_list


def parse_genre(fname):
    """
    Makes a list of genres using csv file
    :param fname: (file) csv file
    :return: (list) a list of genres
    """
    # opens and reads file
    file1 = open(fname, "r", encoding='utf-8')
    reading = csv.reader(file1)
    next(reading)

    # establishes new list
    genre_year_list = []

    # for each line in file...
    for line in file1:
        count = 0
        genre = ""
        genre_found = True

        # analyze character in file line until specific set of characters is found
        for i in range(len(line)):
            if line[i] == "," and line[i + 1] != " ":
                count += 1

            # when found, add those characters to a list of genres
            elif count == 17 and line[i] != "," and line[i] != '"' and genre_found is True:
                if line[i] in whitespace:
                    genre_year_list.append(genre)
                    genre_found = False
                    genre = ""
                else:
                    genre += line[i]
    genre_year_list = list(filter(None, genre_year_list))

    # joining genre categories that have two words, and addressing bug in csv file
    for num in range(len(genre_year_list)):
        if genre_year_list[num] == "hip":
            genre_year_list[num] = "hip-hop"
        if genre_year_list[num] == "easy":
            genre_year_list[num] = "easy-listening"
        elif genre_year_list[num] == "set()":
            genre_year_list[num] = "Other"

    # return finalized version of genre_year_list
    return genre_year_list


def parse_year(fname):
    """
    Makes a list of years listed in a csv file
    :param fname: (csv) a csv file
    :return: (list) a list of years
    """
    # opens file and reads it
    file1 = open(fname, "r", encoding='utf-8')
    reading = csv.reader(file1)
    next(reading)

    # takes all song length info
    song_year_list = []

    # for each line in file...
    for line in file1:
        count = 0
        year = ""

        # write into list when certain characters are found (also addresses indexing bug)
        for i in range(len(line)):
            if line[i] == "," and line[i + 1] != " ":
                count += 1
            elif count == 4 and line[i] != ",":
                year += line[i]
        song_year_list.append(int(year))

    # return final list of years
    return song_year_list


def sort_lengths(list):
    """
    Helper function to sort specific lengths from smallest to largest
    :param list: (list) a list of song lengths
    :return: (list) a list of values that represents number of sorted lengths
    """
    # establishes number of list entries
    length_list = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

    # for each song length in list of lengths, divide by 30000 and add to corresponding spot in new list
    for length in list:
        category = int(length) // 30000
        length_list[category - 1] += 1

    # return new list of values that represent sorted lengths
    return length_list


def length_year_analysis(fname, song_length):
    """
    Creates list of lists of lengths and years
    :param song_length: (list) a list of song lengths
    :param fname: (file) a csv file
    :return: (dict) a dictionary with average lengths as values and years as keys
    """
    # calls helper to parse years
    song_year_list = parse_year(fname)

    # establishes new dictionary
    year_length_dict = {}

    # for each item in list of years...
    for elem in range(len(song_year_list)):

        # add new entry to dictionary if no entry exists
        if song_year_list[elem] not in year_length_dict:
            key = song_year_list[elem]
            year_length_dict.setdefault(key, []).append(int(song_length[elem]))

        # add to already present dictionary entry
        else:
            key = song_year_list[elem]
            year_length_dict.setdefault(key, []).append(int(song_length[elem]))

    # average out values in dictionary
    for key in year_length_dict.keys():
        song_list = year_length_dict[key]
        avg_length = mean(song_list)
        year_length_dict[key] = avg_length

    # sort dictionary by year
    new_year_length_dict = {}
    for num in range(1998, 2020):
        for key in year_length_dict.keys():
            if num == int(key):
                new_value = year_length_dict[key]
                new_year_length_dict[key] = new_value

    # returns finalized dictionary of years and average lengths
    return new_year_length_dict


def length_genre_analysis(length_info, genre_info):
    """
    Creates a dictionary of average genre lengths
    :param length_info: (list) a list of lengths
    :param genre_info: (list) a list of genres
    :return: (dict) a list with genres as keys and lengths as values
    """
    # creates dict with genre keys and list values of song lengths for the corresponding genre
    genre_length_dict = {}
    for elem in range(len(genre_info)):
        if genre_info[elem] not in genre_length_dict:  # creates new dict key
            key = genre_info[elem]
            genre_length_dict.setdefault(key, []).append(int(length_info[elem]))
        else:  # adds onto existing dict key
            key = genre_info[elem]
            genre_length_dict.setdefault(key, []).append(int(length_info[elem]))

    # for each key in dictionary, average out values
    for key in genre_length_dict.keys():
        song_list = genre_length_dict[key]
        avg_length = mean(song_list)
        genre_length_dict[key] = avg_length

    # return final organized dictionary
    return genre_length_dict


def synthesize_data(dict):
    """
    Synthesizes dictionary into a list of lists
    :param dict: (dict) a dictionary
    :return: (list) a list of lists
    """
    # establishes new list
    new_list = []

    # takes each individual key and makes a list out of it and its corresponding value
    for key in dict.keys():
        entry = []
        entry.append(key)
        entry.append(dict[key])
        new_list.append(entry)

    # a list of lists containing keys and values
    return new_list


def plot_graph(data1, data2, labelx, labely, type, title):
    """
    Plots a dataset on a graph
    :param data1: (list) list used for x-axis
    :param data2: (list) list used for y-axis
    :param labelx: (str) x-axis label
    :param labely: (str) y-axis label
    :param type: (int) indicates analysis to be completed
    :param title: (str) desired title of graph
    :return: None
    """
    if type == 1:
        # establishes x and y axis
        x = data2
        y = data1

        # adjusts font size for column labels (so all can fit)
        plt.rcParams.update({'font.size': 8})

        # plots the x and y lists
        plt.bar(x, y, 1, 0, align="edge", edgecolor="black", label="Number of Songs")

        # plots legend, labels, titles, shows plot
        plt.xlabel(labelx)
        plt.ylabel(labely)
        plt.title(title)
        plt.legend()
        plt.savefig("visualization1.png")
        plt.show()

    if type == 2:

        # establishes x and y lists
        x = []
        y = []

        # appends each key to x and its value to y
        for key in data1:
            x.append(key)
            y.append(data1[key])

        # plots x and y lists
        plt.plot(x, y, "b-", label="Average Song Length")
        plt.xlabel(labelx)
        plt.ylabel(labely)
        plt.title(title)
        plt.legend()
        plt.savefig("visualization2.png")
        plt.show()

    if type == 3:

        # establishes x and y lists
        x = []
        y = []

        # for each list in list of lists, append first value to x and second value to y
        for i in range(len(data1)):
            x.append(data1[i][0])
            y.append(data1[i][1])

        # adjusts font size for column labels (so all can fit)
        plt.rcParams.update({'font.size': 4.75})

        # plots x and y lists
        plt.bar(x, y, 1, 0, align="center", edgecolor="black", label="Average Song Length")
        plt.xlabel(labelx)
        plt.ylabel(labely)
        plt.title(title)
        plt.legend()
        plt.savefig("visualization3.png")
        plt.show()


def main():
    """
    Creates a graph of user's choosing indicating correlation between number of songs and their lengths,
    the change in average song length over time, and the average song length of different genres
    """
    # acquires song lengths of all 2000 songs
    song_lengths = parse_song_length("songs_normalize.csv")

    for num in range(1, 4):
        # question 1
        if num == 1:

            # sorts song lengths into different intervals
            list_length = sort_lengths(song_lengths)

            # second intervals used as x-axis columns
            time_list = []
            time = 0
            for times in range(17):
                time_list.append(str(time))
                time += 30

            # prints numeric data
            print("1. Song Length vs. Number of Songs")
            print("The following is our numeric data for this analysis:")
            print(list_length)
            print(time_list)

            # plots graph
            plot_graph(list_length, time_list, "Song Length (Seconds)", "Number of Songs", 1, "Song length vs Number of Songs")

        # question 2
        elif num == 2:

            # makes a dictionary of years and average lengths
            length_year_d = length_year_analysis("songs_normalize.csv", song_lengths)

            # prints numeric data
            print("2. Year vs. Average Song Length")
            print("The following is our numeric data for this analysis:")
            print(length_year_d)

            # plots dictionary of years and average lengths
            plot_graph(length_year_d, None, "Years", "Average Song Length", 2, "Years vs Average Song Length")

        # question 3
        elif num == 3:

            # finds genres from csv file
            genre_info = parse_genre("songs_normalize.csv")

            # makes a list of genres and average lengths
            genres_and_lengths = synthesize_data(length_genre_analysis(song_lengths, genre_info))

            # prints numeric data
            print("3. Genre vs. Average Song Length")
            print("The following is our numeric data for this analysis:")
            print(genres_and_lengths)

            # plots genres and average lengths
            plot_graph(genres_and_lengths, None, "Genres", "Average Song Length", 3, "Genres vs Average Song Length")


if __name__ == '__main__':
    main()

