# Instructions for setting up a mock SMTP server (with Docker)
To run a smtp server, you can use the dockerfile provided to create a image that contains a [MockMock](https://github.com/tweakers/MockMock) server.

1. Go to the folder MockMockDocker. There is a Dockerfile and the MockMock server executable.

2. Create the image using the folowing command.

   ```bash
   docker build -t mockmock <Path/To/Dockerfile>
   ```

   You may change the the name of the image by replacing "mockmock".

3. Your docker file is now ready and you can see it by typing `docker images` (it should appear on the list).

4. Run a container of this images.

   ```bash
   docker run -d -p 8282:8282 -p 2525:2525 mockmock
   ```

   - -d: makes the container run in aground.
   - -p exposes a port used by MockMock. MockMock uses the port 8282 for the web interface and 2525 for the smtp server.
   - Mockmock is the name of the image to run.

5. Now You can go to http://localhost:8282 to see the web interface and connect to the smtp server with localhost and 2525 (for exmple: `telnet localhost 2525`).

