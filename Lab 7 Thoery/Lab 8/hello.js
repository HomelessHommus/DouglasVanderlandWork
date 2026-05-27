class DFA {
    constructor() {
        this.state = "q1";
    }

    run(string) {
        this.state = "q1";

        for (const char of string) {
            if(char === '0'){
                this.state = "q1"
            }
            else if(char === '1') {
                this.state = "q2"
            }
        }

        if(this.state === "q1") {
            return "Accept"
        }
        else if(this.state === "q2"){
            return "Fail"
        }

    }
}

const dfa = new DFA();

console.log(dfa.run("0"));
console.log(dfa.run("1"));
console.log(dfa.run("01010"));
console.log(dfa.run("1011"));




