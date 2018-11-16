var Dog = {
    name: "Miguel",
    age: 4,
    stamina: 10,
    bark: () => {
        this.stamina -= 1;
        console.log("Bark!");
    }
};