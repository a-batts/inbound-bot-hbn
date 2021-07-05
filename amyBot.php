<?php

use Discord\Discord;
use Discord\Parts\Channel\Message;
use React\EventLoop\Factory;

require __DIR__ . '/vendor/autoload.php';

$loop = Factory::create();

$discord = new Discord([
  'token' => '***REMOVED***',
  'loop' => $loop,
]);

$discord->on('message', function (Message $message, Discord $discord) {
  if ($message->content == strtoupper($message->content) && preg_match('/[A-Za-z]/', $message->content))
    $message->reply("don\'t use caps in {$message->channel->name} please \<3");
  elseif (str_contains(strtolower($message->content), 'hi amy'))
    $message->reply("my name is annie \<3");
  elseif (str_contains(strtolower($message->content), 'stock'))
    $message->reply("hi {$message->author->nick} - inboundhealer");
  elseif (strtolower($message->content) == 'a! clearchat'){
    $text = '.' . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL;

    $message->channel->sendMessage($text . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . PHP_EOL . '.', false);
    $message->reply("sorry about that <\3");
  }
});

$discord->run();
