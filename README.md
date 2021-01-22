# Epidemic-Simulator
[Download the APK here](https://drive.google.com/drive/folders/1_-OPqu5pqzBq3KMcJbKA6ZfaDUErxZo5?usp=sharing)

I am currently working on a web implementation of this app, hopefully that will be complete within the next month!

Epidemic Simulator is an educational Android app built with Android Studio, Processing 3, and a tiny bit of Kotlin. My aim was to create an easy to parse, visual way to understand disease spread in a population when no effective treatment is available, such as covid-19. It simulates the spread of a disease within a closed population with the [SIR epidemic model of disease spread](https://www.youtube.com/watch?v=Qrp40ck3WpI), and graphs the data as an epidemic curve. Mask wearing and social distancing have been implemented to demonstrate their effectiveness in flattening the curve of infected individuals. Data visualization is implemented using the [gwOptics Processing library](http://www.gwoptics.org/processing/gwoptics_p5lib/). The settings backdrop was implemented with [roiacult's Backdrop layout library](https://github.com/roiacult/BackdropLayout). I endeavored to follow Materal Design guidelines when building the app as a way to build my eye for design. To ensure stable performance with 1000 agents, I had to implement a [quadtree](https://jimkang.com/quadtreevis/) data structure that was used in my collision detection algorithm. 

## Disclaimer
This simulator is strictly that, a simulator, and an abstract one at that. What it generates isn't 1:1 with what would actually happen in a real world setting, and the variables at play are very simplified. It isn't representative of the way the current COVID-19 epidemic works, and shouldn't be treated as such. It simply gives a visual way of understanding the mechanics of epidemics. 

## How does it work? 
The simulation consists of 100 - 1000 agents that bounce around a canvas in random directions at a set velocity. All but one agent (this is adjustable in settings) are considered *susceptible*. They are able to be infected by the one non-susceptible agent, the *infected*. They have a static radius around them that rolls a chance each day for susceptible agents within their radius to become infected. By default, a day in the simulation is 1 real time second, though this can be adjusted. After a default period of 5 days, an infected agent becomes *removed*, and can't become infected again. 

I've also implemented mask wearing and social distancing. Mask wearing works by reducing the effective infective radius of the agent consistent with the findings [of this study, so I used 80% as an underestimate.](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC7301882/#:~:text=We%20show%20that%20the%20use,%2C%20cumulatively%2C%20during%20cough%20cycles.) This reduces the range an agent can infect a susceptible agent. Social distancing currently functions by setting the velocity of an agent at the start of the simulation to 0, effectively reducing the total number of contacts an agent can have.

The R0 is the base reproductive number, and represents the average number of infections a single infected generates over the population. It changes a lot based on environmental variables, so I've implemented the average R0 over the course of the simulation and the R0 of the current day. 

## Known issues
1) Resetting the simulation randomly freezes either the graph or the simulation. Resetting once or twice more should fix this. 
2) Occasionally the graph line traces spike when the simulation is reset. Again, another reset should fix this.  
3) The app doesn't handle losing focus well, and may exhibit weird behavior like freezing and messing up the agent infectivity timers when returning to it after the screen has been locked or you've returned to it from another app. As before, a reset should fix this. 

## Lessons Learnt
Unfortunately, Processing for Android is not a particularly well documented library, and this there wasn't much information available on why the app is prone to breaking when multitasking. Despite defining a few Android lifecycle override methods that attempted to suspend all simulation rendering manually, it still refuses to be stable. This outcome may have been mitigated somewhat if I had implemented more in depth testing early on. My next step is to port the simulation to the web in React, something substantially better documented. P5.js, the most modern and well documented version of Processing, will also be used, which will improve overall stability.
