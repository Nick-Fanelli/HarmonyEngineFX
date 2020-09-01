let parser = new DOMParser();
let xmlDoc = parser.parseFromString(projectXML, "text/xml");

const project = xmlDoc.children.item(0);

let projectName;
let launcherStateName;

// Util Function
function getTexture(index) {
    for(let i = 0; i < texturesArray.length; i++) {
        if(texturesArray[i].id == index) return texturesArray[i];
    }
}

for(let i = 0; i < project.children.length; i++) {
    if(project.children[i].getAttribute("name") === "ProjectName") projectName = project.children[i].getAttribute("value");
    if(project.children[i].getAttribute("name") === "LauncherState") launcherStateName = project.children[i].getAttribute("value");

    if(project.children[i].nodeName === "Textures") {
        for(let j = 0; j < project.children[i].children.length; j++) {
            let textureElement = project.children[i].children[j];
            let textureID = textureElement.getAttribute("id");
            let textureNameArray = textureElement.getAttribute("path").split("/");
            let textureName = textureNameArray[textureNameArray.length - 1];
            texturesArray.push(new Texture(textureID, textureName));
        }
    }

    if(project.children[i].nodeName === "States") {
        for(let j = 0; j < project.children[i].children.length; j++) {

            let stateI = project.children[i].children[j];
            let stateName;
            let stateObjects = [];

            for(let child = 0; child < stateI.children.length; child++) {
                let childElement = stateI.children[child];

                if(childElement.nodeName === "data" && childElement.hasAttribute("name")) stateName = childElement.getAttribute("name");

                if(childElement.nodeName === "Hierarchy") {
                    let hierarchyElement = childElement;

                    for(let object = 0; object < hierarchyElement.children.length; object++) {
                        let objectElement = hierarchyElement.children[object];

                        let objectName;
                        let objectTextureID;
                        let objectPosition;

                        for(let objectData = 0; objectData < objectElement.children.length; objectData++) {
                            let objectDataElement = objectElement.children[objectData];
                         
                            if(objectDataElement.nodeName === "data" && objectDataElement.hasAttribute("name")) objectName = objectDataElement.getAttribute("name");
                            else if(objectDataElement.nodeName === "data" && objectDataElement.hasAttribute("textureID")) objectTextureID = Number(objectDataElement.getAttribute("textureID"));
                            else if(objectDataElement.nodeName === "vector2f") {
                                objectPosition = new Vector2(Number(objectDataElement.getAttribute("x")), Number(objectDataElement.getAttribute("y")));
                            }
                        }

                        stateObjects.push(new Sprite(objectPosition, new Scale(), getTexture(objectTextureID)));
                    }
                }
            }

            let createdState = new State(stateName);

            for(let obj = 0; obj < stateObjects.length; obj++) {
                createdState.addGameObject(stateObjects[obj]);
            }

            statesArray.push(createdState);
        }
    }
 }

// Get Ready To Launch
document.title = projectName;

for(let i = 0; i < statesArray.length; i++) {
    if(statesArray[i].name === launcherStateName) {
        launcherState = statesArray[i];
        break;
    }
}

startProject(launcherState);