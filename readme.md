# Exploring Methods and Systems of AI

## Content:

- [Genetic Algorithm](#genetic-algorithm-)
- [Expert System](#expert-system-)
- [Hopfield Neural Network](#hopfield-neural-network-)

## Genetic Algorithm 🧬

### Task: ✈️

Given a list of cities, what is the shortest possible route that visits each city exactly once and returns to the origin
city?

### Functionality:

- Collecting cities to travel on the map of Ukraine 💙💛
- Storing and loading dataset of points from the
  MongoDB <img src="./methods-and-systems-of-ai-web/src/travelling-salesman-web/img/mongodb_transparent.png"  style="height: 1rem"/>
- Tweaking genetic algorithm with different parameters 🤓
- Client can track current state of the server's work and dynamically draw a current shortest path 🖼️
- All statistic stored in
  MongoDB <img src="./methods-and-systems-of-ai-web/src/travelling-salesman-web/img/mongodb_transparent.png"  style="height: 1rem"/>
- Server gather statistic and return a line chart on a request 📈

### Built With

#### Backend part:

- Java 21 <img src="https://cdn.jsdelivr.net/npm/programming-languages-logos/src/java/java.png" style="height: 1rem">
- Spring
  Boot <img src="./methods-and-systems-of-ai-web/src/travelling-salesman-web/img/spring-boot-logo.png"  style="height: 1rem"/>
- Vector API. Preview feature. SIMD machine.⚡
- VirtualThreadPerTaskExecutor 🤖
- StringTemplate ➕
-

MongoDB <img src="./methods-and-systems-of-ai-web/src/travelling-salesman-web/img/mongodb_transparent.png"  style="height: 1rem"/>

- Integration/Unit testing
- Lombok 🏝️
- JFreeChart 📈

#### Frontend part:

- React 🚀
- TypeScript 💪
- HTML 5️⃣
- SCSS 😎

### Preview:

Let's solve 32 points using a genetic algorithm. It would be 31! steps 🤯 without such a method.

<img src="./methods-and-systems-of-ai-web/src/travelling-salesman-web/img/GeneticPreview_img32.gif" alt="work of genetic algorithm on tsp preview" style="width: 70%; display: block;">

## Expert System 👩‍🔬

### Task: 🧪

Create a versatile rule-based expert system capable of providing automated expert-level decision support across diverse
domains. Easily extendable with new rules.

### Functionality:

**🎓Rule Management:** Easily add new rules to the knowledge base and establish connections between existing and new
rules.

**🧠Rule Types:** Supports various rule types, including yes/no and formula-based rules. Formula-based rules allow
experts to define formulas with variables, and users can input values for these variables, enabling dynamic
decision-making.

**🔍Keyword Search:** A powerful search feature enables users to find specific rules by entering keywords, offering a
search experience similar to Google.

**💬Interactive Chat:** Engage in chat conversations with the system, providing user input for decision-making. The
system dynamically processes the information provided by the user and offers expert-level decisions based on the rules
in its knowledge base.

### Built With

#### Backend part:

- Java 21 <img src="https://cdn.jsdelivr.net/npm/programming-languages-logos/src/java/java.png" style="height: 1rem">
- Spring
  Boot <img src="./methods-and-systems-of-ai-web/src/travelling-salesman-web/img/spring-boot-logo.png"  style="height: 1rem"/>
- Neo4j <img src="./methods-and-systems-of-ai-web/public/neo4j.png" style="height: 1rem"/>
- GraalVM ScriptEngine. _It is used to calculate the formulas provided by the expert._
- Lombok 🏝️
- Integration/Unit testing 🤯

#### Frontend part:

- React 🚀
- TypeScript 💪
- HTML 5️⃣
- SCSS 😎

### Preview:

#### Expert mode

<img alt="rule definition screenshot" src="./methods-and-systems-of-ai-web/public/rule_definition.png" style="width: 75%; margin-bottom: 0"/>
<img alt="action definition screenshot" src="./methods-and-systems-of-ai-web/public/actions.png" style="width: 75%; display: block"/>

#### User mode

<img alt="search rule screenshot" src="./methods-and-systems-of-ai-web/public/search.png" style="width: 75%"/>
<img alt="chat with expert system screenshot" src="./methods-and-systems-of-ai-web/public/chat.png" style="width: 75%; display: block"/>

### Hopfield Neural Network 🧠

Unveil the potential of the Hopfield Neural Network for pattern recall, whether from images or direct matrix
representations. This versatile neural network, inspired by human memory functions, excels at recognizing patterns
embedded within various data forms.

### Functionality:

**🖌️ Drawing within a Front-end Page:** Users can draw patterns on the webpage or submit matrix representations directly
to the server for processing.

**📷 Image and Matrix Processing:** Receive images or matrix data from clients, convert images to grayscale, and process
matrices to recognize embedded patterns.

**👀 Pattern Recognition:** Submit fragmented or incomplete patterns, either in image or matrix form, for restoration.
Simply specify the network name to trigger pattern retrieval from PostgreSQL for efficient reconstruction.

### Built With

#### Backend part:

- Java 21 <img src="https://cdn.jsdelivr.net/npm/programming-languages-logos/src/java/java.png" style="height: 1rem">
- Spring Boot <img src="./methods-and-systems-of-ai-web/src/travelling-salesman-web/img/spring-boot-logo.png"  style="height: 1rem"/>
- PostgreSQL 🐘
- Lombok 🏝️

#### Frontend part:

- React 🚀
- TypeScript 💪
- HTML 5️⃣
- SCSS 😎

#### Preview:

##### Image processing:

Create network1:
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/poets_init.png" style="width: 37%; display: block;" alt="image processing preview">
Fragment of pattern1:
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/find_pattern1.png" style="width: 37%; display: block;" alt="image processing preview">
Recalled pattern1:
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/recalled_pattern1.png" style="width: 37%; display: block;" alt="image processing preview">
Fragment of pattern2:
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/find_pattern2.png" style="width: 37%; display: block;" alt="image processing preview">
Recalled pattern2:
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/recalled_pattern2.png" style="width: 37%; display: block;" alt="image processing preview">
Create network2:
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/letters_init.png" style="width: 37%; display: block;" alt="image processing preview">
Fragments of letters:
<div style="display: flex">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/find_a_pattern.png" style="width: 37%;margin-right: 2rem; display: block" alt="image processing preview">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/find_c_pattern.png" style="width: 37%; display: block" alt="image processing preview">
</div>
Recalled letters:
<div style="display: flex; ">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/recalled_a_pattern.png" style="width: 37%;margin-right: 2rem; display: block" alt="image processing preview">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/img_pattern/recalled_c_pattern.png" style="width: 37%; display: block" alt="image processing preview">
</div>

##### Drawn patterns:

You can easily draw patterns on the webpage.
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/hello_world.png" style="width: 37%; display: block;" alt="image processing preview">
Create network:
<div style="display: flex;">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/init_h.png" style="width: 20%; display: block" alt="image processing preview">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/init_k.png" style="width: 20%; display: block" alt="image processing preview">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/init_l.png" style="width: 20%; display: block" alt="image processing preview">
</div>
Fragments of letters:
<div style="display: flex; ">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/find_h.png" style="width: 20%; display: block" alt="image processing preview">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/find_k.png" style="width: 20%; display: block" alt="image processing preview">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/find_l.png" style="width: 20%; display: block" alt="image processing preview">
</div>
Recalled letters:
<div style="display: flex; ">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/recalled_h.png" style="width: 20%; display: block" alt="image processing preview">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/recalled_k.png" style="width: 20%; display: block" alt="image processing preview">
<img src="./methods-and-systems-of-ai-web/src/hopfield-neural-network-web/img/drawn_pattern/recalled_l.png" style="width: 20%; display: block" alt="image processing preview">
</div>