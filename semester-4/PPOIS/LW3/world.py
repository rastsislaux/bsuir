import json

import pygame
import pytmx as pytmx

from camera import Camera
from coin_debris import CoinDebris
from const import WINDOW_WIDTH, WINDOW_HEIGHT
from event import Event
from flag import Flag
from goomba import Goomba
from mob import Mob
from platform import Platform
from platform_debris import PlatformDebris
from player import Player
from text import Text
from tube import Tube
from ui import GameUI


class World:

    mob_type = {
        "goomba": Goomba
    }

    def __init__(self, world_name):
        self.obj = []
        self.obj_bg = []
        self.tubes = []
        self.debris = []
        self.mobs = []
        self.projectiles = []
        self.text_objects = []
        self.map = 0
        self.flag = None

        self.map_size = (0, 0)
        self.sky = 0
        self.textures = {}
        self.name = world_name
        self.mobs_to_spawn = None

        self.is_mob_spawned = [False, False]
        self.score_for_killing_mob = 100
        self.score_time = 0

        self.in_event = False
        self.tick = 0
        self.time = 400

        self.loadWorld_11()

        self.player = Player(128, 351)
        self.camera = Camera(self.map_size[0] * 32, 14)
        self.event = Event()
        self.ui = GameUI()

    def render_world(self, game):
        game.screen.blit(self.sky, (0, 0))

        for obj_group in (self.obj_bg, self.obj):
            for obj in obj_group:
                obj.render(game)

        for tube in self.tubes:
            tube.render(game)

    def loadWorld_11(self):
        tmx_data = pytmx.util_pygame.load_pygame("worlds/1-1/W11.tmx")
        with open("worlds/1-1/W11.json") as json_file:
            json_data = json.loads(json_file.read())

        self.map_size = (tmx_data.width, tmx_data.height)
        self.sky = pygame.Surface((WINDOW_WIDTH, WINDOW_HEIGHT))
        self.sky.fill((pygame.Color(json_data["sky_color"])))
        self.mobs_to_spawn = json_data["mobs"]

        self.map = [[0] * tmx_data.height for _ in range(tmx_data.width)]

        layer_num = 0
        for layer in tmx_data.visible_layers:
            for y in range(tmx_data.height):
                for x in range(tmx_data.width):
                    image = tmx_data.get_tile_image(x, y, layer_num)
                    if image is not None:
                        tile_id = tmx_data.get_tile_gid(x, y, layer_num)

                        if layer.name == "Foreground":
                            if tile_id == 22:
                                image = (
                                    image,
                                    tmx_data.get_tile_image(0, 15, layer_num),
                                    tmx_data.get_tile_image(1, 15, layer_num),
                                    tmx_data.get_tile_image(2, 15, layer_num)
                                )

                            self.map[x][y] = Platform(x * tmx_data.tileheight, y * tmx_data.tilewidth, image, tile_id)
                            self.obj.append(self.map[x][y])
            layer_num += 1

        for tube in json_data["tubes"]:
            self.spawn_tube(tube["x"], tube["y"])

        self.flag = Flag(json_data["flag"]["x"], json_data["flag"]["y"])
        
    def reset(self, reset_all):
        self.obj = []
        self.obj_bg = []
        self.tubes = []
        self.debris = []
        self.mobs = []
        self.is_mob_spawned = [False, False]

        self.in_event = False
        self.flag = None
        self.sky = None
        self.map = None

        self.tick = 0
        self.time = 400

        self.map_size = (0, 0)
        self.textures = {}
        self.loadWorld_11()

        self.event.reset()
        self.player.reset(reset_all)
        self.camera.reset()
        
    def spawn_score_text(self, x, y, score=None):
        if score is None:
            self.text_objects.append(Text(str(self.score_for_killing_mob), 16, (x, y)))

            self.score_time = pygame.time.get_ticks()
            if self.score_for_killing_mob < 1600:
                self.score_for_killing_mob *= 2

        else:
            self.text_objects.append(Text(str(score), 16, (x, y)))
            
    def remove_object(self, object):
        self.obj.remove(object)
        self.map[object.rect.x // 32][object.rect.y // 32] = 0

    def remove_whizbang(self, whizbang):
        self.projectiles.remove(whizbang)

    def remove_text(self, text_object):
        self.text_objects.remove(text_object)

    def update_player(self, game):
        self.player.update(game)
        
    def update_entities(self, game):
        for mob in self.mobs:
            mob.update(game)
            if not self.in_event:
                self.entity_collisions(game)
                
    def update_time(self, game):
        if not self.in_event:
            self.tick += 1
            if self.tick % 40 == 0:
                self.time -= 1
                self.tick = 0
            if self.time == 100 and self.tick == 1:
                game.sound_manager.start_fast_music(game)
            elif self.time == 0:
                self.player_death(game)
                
    def update_score_time(self):
        if self.score_for_killing_mob != 100:
            if pygame.time.get_ticks() > self.score_time + 750:
                self.score_for_killing_mob //= 2
                
    def entity_collisions(self, game):
        if not game.world.player.unkillable:
            for mob in self.mobs:
                mob.check_collision_with_player(game)
                
    def player_death(self, game):
        self.in_event = True
        self.player.reset_jump()
        self.player.reset_move()
        self.player.num_of_lives -= 1

        if self.player.num_of_lives == 0:
            self.event.start_kill(game, game_over=True)
        else:
            self.event.start_kill(game, game_over=False)
            
    def player_win(self, game):
        self.in_event = True
        self.player.reset_jump()
        self.player.reset_move()
        self.event.start_win(game)

    def spawn_tube(self, x_coords, y_coords):
        self.tubes.append(Tube(x_coords, y_coords))

        for y in range(y_coords, 12):
            for x in range(x_coords, x_coords + 2):
                self.map[x][y] = Platform(x * 32, y * 32, image=None, type_id=0)

    def spawn_debris(self, x, y, type):
        if type == 0:
            self.debris.append(PlatformDebris(x, y))
        elif type == 1:
            self.debris.append(CoinDebris(x, y))

    def try_spawn_mobs(self, game):
        for pack in self.mobs_to_spawn:
            if game.world.player.pos_x > pack["player_x_pos"] and not pack.get("spawned"):
                kind = self.mob_type.get(pack["type"])
                if kind is None:
                    raise RuntimeError(f"Unknown mob type: {pack['type']}")
                for mob in pack["pos"]:
                    self.mobs.append(kind(mob["x"], mob["y"], mob["direction"]))
                pack["spawned"] = True
        
    def update(self, game):
        self.update_entities(game)

        if not game.world.in_event:
            if self.player.in_level_up_animation:
                self.player.change_powerlvl_animation()
            elif self.player.in_level_down_animation:
                self.player.change_powerlvl_animation()
                self.update_player(game)
            else:
                self.update_player(game)
        else:
            self.event.update(game)

        for debris in self.debris:
            debris.update(game)

        for whizbang in self.projectiles:
            whizbang.update(game)

        for text_object in self.text_objects:
            text_object.update(game)

        if not self.in_event:
            self.camera.update(game.world.player.rect)

        self.try_spawn_mobs(game)

        self.update_time(game)
        self.update_score_time()

    def get_blocks_for_collision(self, x, y):
        return (
            self.map[x][y - 1],
            self.map[x][y + 1],
            self.map[x][y],
            self.map[x - 1][y],
            self.map[x + 1][y],
            self.map[x + 2][y],
            self.map[x + 1][y - 1],
            self.map[x + 1][y + 1],
            self.map[x][y + 2],
            self.map[x + 1][y + 2],
            self.map[x - 1][y + 1],
            self.map[x + 2][y + 1],
            self.map[x][y + 3],
            self.map[x + 1][y + 3]
        )

    def get_blocks_below(self, x, y):
        return (
            self.map[x][y + 1],
            self.map[x + 1][y + 1]
        )

    def render(self, game):
        game.screen.blit(self.sky, (0, 0))

        for obj in self.obj_bg:
            obj.render(game)

        for mob in self.mobs:
            mob.render(game)

        for obj in self.obj:
            obj.render(game)

        for tube in self.tubes:
            tube.render(game)

        for whizbang in self.projectiles:
            whizbang.render(game)

        for debris in self.debris:
            debris.render(game)

        self.flag.render(game)

        for text_object in self.text_objects:
            text_object.render_in_game(game)

        self.player.render(game)

        self.ui.render(game)
