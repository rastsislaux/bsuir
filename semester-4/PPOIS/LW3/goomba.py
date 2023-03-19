import pygame

from const import GRAVITY
from mob import Mob


class Goomba(Mob):

    def __init__(self, x, y, move_direction):
        super().__init__()
        self.rect = pygame.Rect(x, y, 32, 32)

        if move_direction:
            self.x_vel = 1
        else:
            self.x_vel = -1

        self.crushed = False

        self.current_image = 0
        self.image_tick = 0
        self.images = [
            pygame.image.load("images/goombas_0.png").convert_alpha(),
            pygame.image.load("images/goombas_1.png").convert_alpha(),
            pygame.image.load("images/goombas_dead.png").convert_alpha()
        ]

        self.images.append(pygame.transform.flip(self.images[0], 0, 180))

    def die(self, game, instantly, crushed):
        if not instantly:
            game.world.player.add_score(game.world.score_for_killing_mob)
            game.world.spawn_score_text(self.rect.x + 16, self.rect.y)

            if crushed:
                self.crushed = True
                self.image_tick = 0
                self.image_tick = 2
                self.state = -1
                game.sound_manager.play("kill_mob", 0, 0.5)
                self.collision = False

            else:
                self.y_vel = -4
                self.current_image = 3
                game.sound_manager.play("shot", 0, 0.5)
                self.state = -1
                self.collision = False

        else:
            game.world.mobs.remove(self)

    def check_collision_with_player(self, game):
        if self.collision:
            if self.rect.colliderect(game.world.player.rect):
                if self.state != -1:
                    if game.world.player.y_vel > 0:
                        self.die(game, instantly=False, crushed=True)
                        game.world.player.reset_jump()
                        game.world.player.jump_on_mob()
                    else:
                        if not game.world.player.unkillable:
                            game.world.player.set_powerlvl(0, game)

    def update_image(self):
        self.image_tick += 1
        if self.image_tick == 14:
            self.current_image = 1
        elif self.image_tick == 28:
            self.current_image = 0
            self.image_tick = 0

    def update(self, game):
        if self.state == 0:
            self.update_image()

            if not self.on_ground:
                self.y_vel += GRAVITY

            blocks = game.world.get_blocks_for_collision(int(self.rect.x // 32), int(self.rect.y // 32))
            self.update_x_pos(blocks)
            self.update_y_pos(blocks)

            self.check_map_borders(game)

        elif self.state == -1:
            if self.crushed:
                self.image_tick += 1
                if self.image_tick == 50:
                    game.world.mobs.remove(self)
            else:
                self.y_vel += GRAVITY
                self.rect.y += self.y_vel
                self.check_map_borders(game)

    def render(self, game):
        game.screen.blit(self.images[self.current_image], game.world.camera.apply(self))
