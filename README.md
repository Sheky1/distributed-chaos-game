# Distributed Chaos Game

A functional distributed system that calculates (draws) fractal structures using the chaos game. It's a self-organizing distributed system, that supports failure of nodes.

[!Triangle](./first_job.png)

The fractal are split up between the nodes and each node draws a shape it's in charge of. Once a new node enters the system, the nodes reorganize in order to split the work up inside the system, to make it faster.

[!Square](./square_job.png)
