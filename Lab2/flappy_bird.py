import random
import sys
from pathlib import Path

import pygame

# -------------------------
# Config
# -------------------------
WIDTH, HEIGHT = 360, 640
FPS = 60
GRAVITY = 0.35
FLAP_STRENGTH = -7.0
PIPE_SPEED = 3
PIPE_GAP = 150
PIPE_SPAWN_MS = 1300


def locate_asset_dir() -> Path:
    """Find folder containing game PNG assets."""
    script_dir = Path(__file__).resolve().parent
    if (script_dir / "flappybirdbg.png").exists():
        return script_dir

    cwd = Path.cwd()
    if (cwd / "flappybirdbg.png").exists():
        return cwd
    if (cwd / "Lab2" / "flappybirdbg.png").exists():
        return cwd / "Lab2"

    raise FileNotFoundError("Khong tim thay asset PNG trong thu muc Lab2/")


def main() -> None:
    asset_dir = locate_asset_dir()

    pygame.init()
    pygame.display.set_caption("Flappy Bird")
    # Fixed-size window (non-resizable by default without RESIZABLE flag)
    screen = pygame.display.set_mode((WIDTH, HEIGHT))
    clock = pygame.time.Clock()
    font = pygame.font.SysFont("arial", 28, bold=True)
    small_font = pygame.font.SysFont("arial", 20)

    bg_img = pygame.image.load(str(asset_dir / "flappybirdbg.png")).convert()
    bird_img = pygame.image.load(str(asset_dir / "flappybird.png")).convert_alpha()
    top_pipe_img = pygame.image.load(str(asset_dir / "toppipe.png")).convert_alpha()
    bottom_pipe_img = pygame.image.load(str(asset_dir / "bottompipe.png")).convert_alpha()
    bg_img = pygame.transform.scale(bg_img, (WIDTH, HEIGHT))

    class Bird:
        def __init__(self, x: int, y: int):
            self.image = bird_img
            self.x = x
            self.y = float(y)
            self.vel = 0.0
            self.rect = self.image.get_rect(center=(self.x, int(self.y)))

        def flap(self):
            self.vel = FLAP_STRENGTH

        def update(self):
            self.vel += GRAVITY
            self.y += self.vel
            self.rect.center = (self.x, int(self.y))

        def draw(self, surface):
            surface.blit(self.image, self.rect)

    class Pipe:
        def __init__(self, x: int, gap_y: int):
            self.x = x
            self.gap_y = gap_y
            self.passed = False
            self.top_rect = top_pipe_img.get_rect(
                midbottom=(self.x, self.gap_y - PIPE_GAP // 2)
            )
            self.bottom_rect = bottom_pipe_img.get_rect(
                midtop=(self.x, self.gap_y + PIPE_GAP // 2)
            )

        def update(self):
            self.x -= PIPE_SPEED
            self.top_rect.centerx = self.x
            self.bottom_rect.centerx = self.x

        def draw(self, surface):
            surface.blit(top_pipe_img, self.top_rect)
            surface.blit(bottom_pipe_img, self.bottom_rect)

        def off_screen(self):
            return self.top_rect.right < 0

    def create_pipe() -> Pipe:
        min_gap_y = 170
        max_gap_y = HEIGHT - 170
        gap_y = random.randint(min_gap_y, max_gap_y)
        return Pipe(WIDTH + 40, gap_y)

    def reset_game():
        bird = Bird(80, HEIGHT // 2)
        pipes = []
        score = 0
        game_over = False
        return bird, pipes, score, game_over

    bird, pipes, score, game_over = reset_game()
    last_pipe_time = pygame.time.get_ticks()

    running = True
    while running:
        clock.tick(FPS)

        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                running = False
            elif event.type == pygame.KEYDOWN:
                if event.key in (pygame.K_SPACE, pygame.K_RETURN) and not game_over:
                    bird.flap()
                elif event.key == pygame.K_r and game_over:
                    bird, pipes, score, game_over = reset_game()
                    last_pipe_time = pygame.time.get_ticks()

        if not game_over:
            now = pygame.time.get_ticks()
            if now - last_pipe_time >= PIPE_SPAWN_MS:
                pipes.append(create_pipe())
                last_pipe_time = now

            bird.update()

            for pipe in pipes:
                pipe.update()
                if not pipe.passed and pipe.x < bird.x:
                    pipe.passed = True
                    score += 1

            pipes = [p for p in pipes if not p.off_screen()]

            for pipe in pipes:
                if bird.rect.colliderect(pipe.top_rect) or bird.rect.colliderect(
                    pipe.bottom_rect
                ):
                    game_over = True
                    break

            if bird.rect.top <= 0 or bird.rect.bottom >= HEIGHT:
                game_over = True

        screen.blit(bg_img, (0, 0))
        for pipe in pipes:
            pipe.draw(screen)
        bird.draw(screen)

        score_text = font.render(f"Score: {score}", True, (255, 255, 255))
        screen.blit(score_text, (12, 10))

        if game_over:
            over_text = font.render("GAME OVER", True, (255, 60, 60))
            restart_text = small_font.render("Nhan R de choi lai", True, (255, 255, 255))
            screen.blit(over_text, (WIDTH // 2 - over_text.get_width() // 2, HEIGHT // 2 - 40))
            screen.blit(
                restart_text,
                (WIDTH // 2 - restart_text.get_width() // 2, HEIGHT // 2 + 5),
            )

        pygame.display.flip()

    pygame.quit()
    sys.exit()


if __name__ == "__main__":
    main()
