package com.deeep.spaceglad.managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.TextureProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.deeep.spaceglad.bullet.MotionState;
import com.deeep.spaceglad.components.*;
import com.deeep.spaceglad.systems.BulletSystem;
import com.deeep.spaceglad.GameWorld;
import com.deeep.spaceglad.Assets;
/**
 * Created by Elmar on 7-8-2015.
 */
public class EntityFactory {
    private static Model playerModel;
    private static Texture playerTexture;
    private static ModelBuilder modelBuilder;
    private static Model boxModel;

    static {
        modelBuilder = new ModelBuilder();
        playerTexture = new Texture(Gdx.files.internal("data/badlogic.jpg"));
        Material material = new Material(TextureAttribute.createDiffuse(playerTexture), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
        playerModel = modelBuilder.createCapsule(2f, 4f, 16, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        boxModel = modelBuilder.createBox(1f, 1f, 1f, new Material(ColorAttribute.createDiffuse(Color.WHITE),
                ColorAttribute.createSpecular(Color.WHITE), FloatAttribute.createShininess(64f)), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }


    public static Entity createStaticEntity(Model model, float x, float y, float z, float rotateDeg) {
        final BoundingBox boundingBox = new BoundingBox();
        model.calculateBoundingBox(boundingBox);
        Vector3 tmpV = new Vector3();
        btCollisionShape col = new btBoxShape(tmpV.set(boundingBox.getWidth() * 0.5f, boundingBox.getHeight() * 0.5f, boundingBox.getDepth() * 0.5f));
        Entity entity = new Entity();
        ModelComponent modelComponent = new ModelComponent(model, x, y, z);
		modelComponent.instance.transform.rotate(0f, 0f, 1f, rotateDeg);
		modelComponent.instance.calculateTransforms();
        entity.add(modelComponent);
        BulletComponent bulletComponent = new BulletComponent();
        bulletComponent.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0, null, col, Vector3.Zero);
        bulletComponent.body = new btRigidBody(bulletComponent.bodyInfo);
        bulletComponent.body.userData = entity;
        bulletComponent.motionState = new MotionState(modelComponent.instance.transform);
        ((btRigidBody) bulletComponent.body).setMotionState(bulletComponent.motionState);
        entity.add(bulletComponent);
        return entity;
    }
	
	public static Entity createDoorEntity(Model model, float x, float y, float z, float rotateDeg, Door d) {
		final BoundingBox boundingBox = new BoundingBox();
        model.calculateBoundingBox(boundingBox);
        Vector3 tmpV = new Vector3();
        btCollisionShape col = new btBoxShape(tmpV.set(boundingBox.getWidth() * 0.5f, boundingBox.getHeight() * 0.5f, boundingBox.getDepth() * 0.5f));
        Entity entity = new Entity();
        ModelComponent modelComponent = new ModelComponent(model, x, y, z);
		modelComponent.instance.transform.rotate(0f, 1f, 0f, rotateDeg);
		modelComponent.instance.calculateTransforms();
        entity.add(modelComponent);
        BulletComponent bulletComponent = new BulletComponent();
        bulletComponent.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0, null, col, Vector3.Zero);
        bulletComponent.body = new btRigidBody(bulletComponent.bodyInfo);
        bulletComponent.body.userData = entity;
        bulletComponent.motionState = new MotionState(modelComponent.instance.transform);
        ((btRigidBody) bulletComponent.body).setMotionState(bulletComponent.motionState);
        entity.add(bulletComponent);
        entity.add(new DoorComponent(d));
        return entity;
	}

    public static Entity createVisualEntity(Model model, float x, float y, float z, float rotateDeg){
                Entity entity = new Entity();
                ModelComponent modelComponent = new ModelComponent(model, x, y, z);
                modelComponent.instance.transform.rotate(0f, 0f, 1f, rotateDeg);
                modelComponent.instance.calculateTransforms();
                entity.add(modelComponent);
                return entity;
           }

    public static Entity createTreeEntity(Model model, float x, float y, float z, float rotateDeg){
        final BoundingBox boundingBox = new BoundingBox();
        model.calculateBoundingBox(boundingBox);
        Vector3 tmpV = new Vector3();
        btCollisionShape col = new btCylinderShape(tmpV.set(boundingBox.getWidth() * 0.15f, boundingBox.getHeight() * 0.9f, boundingBox.getDepth() * 0.5f));
        Entity entity = new Entity();
        ModelComponent modelComponent = new ModelComponent(model, x, y, z);
		modelComponent.instance.transform.rotate(0f, 0f, 1f, rotateDeg);
		modelComponent.instance.calculateTransforms();
        entity.add(modelComponent);
        BulletComponent bulletComponent = new BulletComponent();
        bulletComponent.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0, null, col, Vector3.Zero);
        bulletComponent.body = new btRigidBody(bulletComponent.bodyInfo);
        bulletComponent.body.userData = entity;
        bulletComponent.motionState = new MotionState(modelComponent.instance.transform);
        ((btRigidBody) bulletComponent.body).setMotionState(bulletComponent.motionState);
        entity.add(bulletComponent);
        return entity;
       }

       public static Entity createRampEntity(Model model, float x, float y, float z, float rotateDeg){
        final BoundingBox boundingBox = new BoundingBox();
        model.calculateBoundingBox(boundingBox);
        Vector3 tmpV = new Vector3();
        btCollisionShape col = new btConvexHullShape();
        Entity entity = new Entity();
        ModelComponent modelComponent = new ModelComponent(model, x, y, z);
		modelComponent.instance.transform.rotate(0f, 0f, 1f, rotateDeg);
		modelComponent.instance.calculateTransforms();
        entity.add(modelComponent);
        BulletComponent bulletComponent = new BulletComponent();
        bulletComponent.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0, null, col, Vector3.Zero);
        bulletComponent.body = new btRigidBody(bulletComponent.bodyInfo);
        bulletComponent.body.userData = entity;
        bulletComponent.motionState = new MotionState(modelComponent.instance.transform);
        ((btRigidBody) bulletComponent.body).setMotionState(bulletComponent.motionState);
        entity.add(bulletComponent);
        return entity;
       }

    private static Entity createCharacter(BulletSystem bulletSystem, float x, float y, float z, int type) {
        Entity entity = new Entity();
	ModelComponent modelComponent = null;
	switch(type) {
		case 0: //player
            switch(Assets.avColor){
                case 1: //red
                    modelComponent = new ModelComponent(Assets.playerModelRed, x, y, z);
                    break;
                case 2: //orange
                    modelComponent = new ModelComponent(Assets.playerModelOrange, x, y, z);
                    break;
                case 3: //yellow
                    modelComponent = new ModelComponent(Assets.playerModelYellow, x, y, z);
                    break;
                case 4: //green
                    modelComponent = new ModelComponent(Assets.playerModelGreen, x, y, z);
                    break;
                case 5: //blue
                    modelComponent = new ModelComponent(Assets.playerModelBlue, x, y, z);
                    break;
                case 6: //indigo
                    modelComponent = new ModelComponent(Assets.playerModelIndigo, x, y, z);
                    break;
                case 7: //violet
                    modelComponent = new ModelComponent(Assets.playerModelViolet, x, y, z);
                    break;
                case 8: //gray
                    modelComponent = new ModelComponent(Assets.playerModelGray, x, y, z);
                    break;
                case 9: //black
                    modelComponent = new ModelComponent(Assets.playerModelBlack, x, y, z);
                    break;

            }
            for (Node node : modelComponent.instance.nodes) node.scale.scl(3.8f); // scale the model -Paul
            modelComponent.instance.transform.rotate(0, 1, 0, 0);
            modelComponent.instance.calculateTransforms();
		break;

		//Nick A for HW#6
			case 1: //anklyo model
				modelComponent = new ModelComponent(Assets.anklyoModel, x, y, z);
				entity.add(new AnimationComponent(modelComponent.instance));
			break;
			case 2: //raptor model	
				modelComponent = new ModelComponent(Assets.raptorModel, x, y, z);
				entity.add(new AnimationComponent(modelComponent.instance));
			break;
			//end
	}
	if(modelComponent != null)
		entity.add(modelComponent);
        CharacterComponent characterComponent = new CharacterComponent();
        characterComponent.ghostObject = new btPairCachingGhostObject();
        characterComponent.ghostObject.setWorldTransform(modelComponent.instance.transform);
        characterComponent.ghostShape = new btCapsuleShape(2f, 2f);
        characterComponent.ghostObject.setCollisionShape(characterComponent.ghostShape);
        //characterComponent.ghostObject.setCollisionShape(Bullet.obtainStaticNodeShape(modelComponent.instance.nodes));
        characterComponent.ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
        characterComponent.characterController = new btKinematicCharacterController(characterComponent.ghostObject, characterComponent.ghostShape, .35f);
        characterComponent.ghostObject.userData = entity;
        entity.add(characterComponent);
        bulletSystem.collisionWorld.addCollisionObject(entity.getComponent(CharacterComponent.class).ghostObject,
                (short) btBroadphaseProxy.CollisionFilterGroups.CharacterFilter,
                (short) (btBroadphaseProxy.CollisionFilterGroups.AllFilter));
        bulletSystem.collisionWorld.addAction(entity.getComponent(CharacterComponent.class).characterController);

        entity.add(new AnimationComponent(modelComponent.instance)); //Avatar animation -paul

        return entity;
    }

    public static Entity createPlayer(BulletSystem bulletSystem, float x, float y, float z) {
        Entity entity = createCharacter(bulletSystem, x, y, z, 0);
        entity.add(new PlayerComponent());
        return entity;
    }
	
	// Handles creating health packs
	public static Entity createHealthPack(BulletSystem bulletSystem, float x, float y, float z) {
        Entity entity = new Entity();
		final BoundingBox boundingBox = new BoundingBox();
		Model model = new Model();
		model = Assets.healthPackModel;
		model.calculateBoundingBox(boundingBox);
		
		ModelComponent modelComponent = new ModelComponent(model, x, y, z);
		modelComponent.instance.transform.scale(0.1f, 0.1f, 0.1f);
		modelComponent.instance.calculateTransforms();
		//modelComponent.calculateBoundingBox(boundingBox);
		
		HealthPackComponent healthPackComponent = new HealthPackComponent(HealthPackComponent.STATE.READY, 1);
		
		healthPackComponent.ghostObject = new btPairCachingGhostObject();
        healthPackComponent.ghostObject.setWorldTransform(modelComponent.instance.transform);
        healthPackComponent.ghostShape = new btCapsuleShape(2f, 2f);
        healthPackComponent.ghostObject.setCollisionShape(healthPackComponent.ghostShape);
        healthPackComponent.ghostObject.setCollisionFlags(btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT);
		
		
		entity.add(healthPackComponent);
		
		
        Vector3 tmpV = new Vector3();
        btCollisionShape col = new btBoxShape(tmpV.set(boundingBox.getWidth() * 0.3f, boundingBox.getHeight() * 0.3f, boundingBox.getDepth() * 0.3f));
		
		
		BulletComponent bulletComponent = new BulletComponent();
        bulletComponent.bodyInfo = new btRigidBody.btRigidBodyConstructionInfo(0, null, col, Vector3.Zero);
        bulletComponent.body = new btRigidBody(bulletComponent.bodyInfo);
        bulletComponent.body.userData = entity;
        bulletComponent.motionState = new MotionState(modelComponent.instance.transform);
        ((btRigidBody) bulletComponent.body).setMotionState(bulletComponent.motionState);
        entity.add(bulletComponent);
		
		
		
		entity.add(modelComponent);
		
		
		
        
        return entity;
    }

    public static Entity createEnemy(BulletSystem bulletSystem, float x, float y, float z, int type) {
        if (type != 1 && type != 2)
		type = 1;
	Entity entity = createCharacter(bulletSystem, x,y,z, type);
        entity.add(new EnemyComponent(EnemyComponent.STATE.HUNTING,type));
        entity.add(new StatusComponent());
	    //Nick A for HW#6
		if(entity.getComponent(AnimationComponent.class) != null && entity.getComponent(AnimationComponent.class).getController() != null)
		{
			if(entity.getComponent(AnimationComponent.class).getController().current == null)
				entity.getComponent(AnimationComponent.class).animate("Armature|walk", -1, 3);
		}
		//end
        return entity;
    }
	
	public static Entity loadGun(float x, float y, float z)
	{
		ModelLoader<?> modelLoader = new G3dModelLoader (new JsonReader());
		ModelData modelData = modelLoader.loadModelData(Gdx.files.internal("data/GUNMODEL.g3dj"));
		Model model = new Model(modelData, new TextureProvider.FileTextureProvider());
		ModelComponent modelComponent = new ModelComponent(model, x, y, z);
		modelComponent.instance.transform.rotate(0, 1, 0, 180);
		Entity gunEntity = new Entity();
		gunEntity.add(modelComponent);
		gunEntity.add(new GunComponent());
		gunEntity.add(new AnimationComponent(modelComponent.instance));
		return gunEntity;
	}

//    public static void dispose() {
//        playerModel.dispose();
//        playerTexture.dispose();
//        boxModel.dispose();
//    }
}