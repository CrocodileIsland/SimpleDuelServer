# SimpleDuelServer
The server side code that handles all client connections

********************************************************

First, the Main class calls the startUp() method in the DataHandler class, preparing that class for incoming data.

Then, the Main class creates the server bootstrap and listens for incoming connections.

The NettyServerHandler class is the first class to deal with any incoming connections and data. It's possible to receive fragmented data, so if it happens, the fragmented data string is stored in the User object assossiated with that connection. Then when the next piece of the data string is receieved, that's added to the previous string.

Once a data string is complete and able to be converted to a JSONObject, that data is handled by calling the onData() method from the DataHandler class.
