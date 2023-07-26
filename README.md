# RMI_GUI
you can see the second repo (the reciever) from here https://github.com/aroo530/RMI_Reciever . <br>
Hey everyone! I wanted to share this small project I made for my distributed systems subject in college. The main focus of the project was to use RMI (remote method invocation) and multithreading. So, we were asked to create two JAVA projects that can communicate with each other.

In the project, the UI is in one project, while the processing part is in another project called the server or receiver. On our server, we have five functions that we use to process words and gather statistics like word count and longest word.

Now, for the second objective, we had to use threads and compare their performance to calculate the speed up. I created two versions for this purpose. The first version is threaded, where each function runs on its own thread. The second version is sequential, where the functions run one after the other. We can measure the execution time for each approach and display the speed up achieved.
